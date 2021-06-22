package com.mywork.service;

import com.mywork.model.LoadRequest;
import com.mywork.model.RandomKeyDetails;
import com.mywork.model.Request;
import com.mywork.model.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.*;

@Slf4j
@Service
public class APILoadServiceImpl implements APILoadService {

    @Autowired
    private LoadExecutorService loadExecutorService;

    @Autowired
    private LoadTransformService loadTransformService;

    @Override
    public Response generate(LoadRequest request) {
        this.loadExecutorService.execute(request);
        log.info("Async execution submitted");
        return new Response(LocalDate.now().toString(), "in_progress");
    }

    @Override
    public Response fetch(String executionId) {
        return null;
    }


}
