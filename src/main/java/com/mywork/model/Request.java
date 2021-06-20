package com.mywork.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Data
public class Request {
    private Map<String, String> headers;
    private Map<String, Object> body;
    private String url;
    private String httpMethod;
    private List<RandomKeyDetails> randomGenKeys;

    public Request(Request request) {
        this.headers = request.getHeaders();
        if(Objects.nonNull(request.getBody()) && ! request.getBody().isEmpty()) {
            this.body = new HashMap<>(request.getBody());
        }
        this.url = request.getUrl();
        this.httpMethod = request.getHttpMethod();
        this.randomGenKeys = request.getRandomGenKeys();
    }

    public Request() {

    }
}
