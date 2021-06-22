package com.mywork.model;


import lombok.Data;
import java.util.List;

@Data
public class LoadRequest {
    private int threads;
    private List<Request> requests;
}
