package com.mywork.service;

import com.mywork.model.LoadRequest;
import com.mywork.model.Request;
import com.mywork.model.Response;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

@Slf4j
@Service
public class LoadExecutorServiceImpl implements LoadExecutorService {

    @Autowired
    private LoadTransformService transformService;

    @Override
    public void execute(LoadRequest loadRequest) {
        ExecutorService mainExecutorService = Executors.newSingleThreadExecutor();
        try {
            mainExecutorService.execute(new Runnable() {
                @Override
                public void run() {
                    Logger log = LoggerFactory.getLogger(LoadExecutorServiceImpl.class);
                    List<Request> requestList = loadRequest.getRequests();
                    int totalThreadsRequired = requestList.size() * loadRequest.getThreads();
                    ExecutorService requestExecutor = Executors.newFixedThreadPool(totalThreadsRequired);
                    List<Callable<ResponseEntity<Map>>> allCallables = new ArrayList<>();
                    requestList.forEach(request -> {
                        List<Callable<ResponseEntity<Map>>> currentRequestCallables = prepareSingleRequest(request, loadRequest.getThreads());
                        allCallables.addAll(currentRequestCallables);
                    });
                    AllRequests requests = new AllRequests(requestExecutor, allCallables);
                    executeRequests(requests);
                    log.info("Main executor completed....");
                }
            });
            log.info("Main executor started....");
        } catch (Exception e) {
            log.info("Main executor failure...", e);
        } finally {
            mainExecutorService.shutdown();
            log.info("Shutdown Complete - Main executor");
        }
    }

    private void executeRequests(AllRequests requestDetails) {
        ExecutorService executorService = requestDetails.getExecutorService();
        List<Callable<ResponseEntity<Map>>> callables = requestDetails.getCallables();
        log.info("No.of callables: " + callables.size());
        try {
            List<Future<ResponseEntity<Map>>> futures = executorService.invokeAll(callables);
            for (Future<ResponseEntity<Map>> future : futures) {
                try {
                    log.info("future.isDone = " + future.isDone());
                    ResponseEntity<Map> responseEntity = future.get();
                    log.info("future: response =" + responseEntity);
                } catch (CancellationException | ExecutionException ce) {
                    log.error("Exception occurred while executing request.. ", ce);
                } catch (InterruptedException ie) {
                    log.error("Exception occurred while executing request.. ", ie);
                    Thread.currentThread().interrupt(); // ignore/reset
                }
            }
        } catch (Exception e) {
            log.error("Exception occurred while executing request.. ", e);
        } finally {
            executorService.shutdown();
            log.info("Shutdown complete - request executor");
        }
    }

    private List<Callable<ResponseEntity<Map>>> prepareSingleRequest(Request request, int threads) {
        List<Callable<ResponseEntity<Map>>> callables = new ArrayList<>();
        for (int i = 0; i < threads; i++) {
            Request requestToFire = new Request(request);
            requestToFire = this.transformService.transform(requestToFire);
            Callable<ResponseEntity<Map>> callable = getCallableForExecutingRequest(requestToFire);
            callables.add(callable);
        }
        return callables;
    }

    public Callable<ResponseEntity<Map>> getCallableForExecutingRequest(Request request) {
        return () -> {
            RestTemplate restTemplate = new RestTemplate();
            HttpClient httpClient = HttpClientBuilder.create().build();
            restTemplate.setRequestFactory(new
                    HttpComponentsClientHttpRequestFactory(httpClient));
            HttpHeaders headers = getHeaders(request.getHeaders());
            HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(request.getBody(), headers);
            ResponseEntity<Map> responseEntity = null;
            log.info("Invoking API from thread: " + Thread.currentThread().getId());
            switch (request.getHttpMethod()) {
                case "POST":
                case "post":
                    responseEntity = restTemplate.exchange(request.getUrl(), HttpMethod.POST, httpEntity, Map.class);
                    break;
                case "GET":
                case "get":
                    responseEntity = restTemplate.exchange(request.getUrl(), HttpMethod.GET, httpEntity, Map.class);
                    break;
                case "PATCH":
                case "patch":
                    responseEntity = restTemplate.exchange(request.getUrl(), HttpMethod.PATCH, httpEntity, Map.class);
                    break;
                case "PUT":
                case "put":
                    responseEntity = restTemplate.exchange(request.getUrl(), HttpMethod.PUT, httpEntity, Map.class);
                    break;
            }
            return responseEntity;
        };
    }

    private HttpHeaders getHeaders(Map<String, String> headers) {
        HttpHeaders apiHeaders = new HttpHeaders();
        headers.forEach(apiHeaders::set);
        return apiHeaders;
    }

    @Data
    static class AllRequests {
        ExecutorService executorService;
        List<Callable<ResponseEntity<Map>>> callables;

        AllRequests(ExecutorService executorService, List<Callable<ResponseEntity<Map>>> callables) {
            this.callables = callables;
            this.executorService = executorService;
        }
    }
}
