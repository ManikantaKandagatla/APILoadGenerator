package com.mywork.service;


import com.mywork.model.LoadRequest;
import com.mywork.model.Response;

public interface APILoadService {
    Response generate(LoadRequest request);
    Response fetch(String executionId);
}
