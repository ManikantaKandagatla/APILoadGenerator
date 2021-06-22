package com.mywork.controller;

import com.mywork.model.LoadRequest;
import com.mywork.model.Response;
import com.mywork.service.APILoadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoadController {

    @Autowired
    private APILoadService apiLoadService;

    @RequestMapping(method = RequestMethod.POST, path = "/load")
    public Response submitLoad(@RequestBody LoadRequest loadRequest) {
        return apiLoadService.generate(loadRequest);
    }
}
