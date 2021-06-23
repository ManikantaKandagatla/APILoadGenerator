package com.mywork.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@Component
@Slf4j
public class DummyScheduler {

    private RestTemplate restTemplate;

//    public DummyScheduler() {
//        this.restTemplate = new RestTemplate();
//    }

    @Scheduled(fixedRate = 240000)
    public void fixedRateSch() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

        Date now = new Date();
        String strDate = sdf.format(now);
        log.info("Fixed Rate scheduler:: " + strDate);
//        HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(null, null);
//        String baseUrl = System.getProperty("APP_BASE_URL", "http:");
//        ResponseEntity<Object> responseEntity = this.restTemplate.exchange(baseUrl, HttpMethod.GET, httpEntity, Object.class);
//        log.info("Response: " + responseEntity.getBody());
    }
}