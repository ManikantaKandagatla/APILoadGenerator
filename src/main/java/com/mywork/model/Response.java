package com.mywork.model;

import lombok.Data;

@Data
public class Response {
    private String executionId;
    private String status;

    public Response(String executionId, String status) {
        this.executionId = executionId;
        this.status = status;
    }
}
