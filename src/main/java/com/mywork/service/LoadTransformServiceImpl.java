package com.mywork.service;

import com.mywork.model.RandomKeyDetails;
import com.mywork.model.Request;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;

@Service
public class LoadTransformServiceImpl implements LoadTransformService {

    @Override
    public Request transform(Request request) {
        List<RandomKeyDetails> details = request.getRandomGenKeys();
        if (Objects.nonNull(request.getRandomGenKeys()) && !details.isEmpty()) {
            details.forEach(detail -> {
                Object randomVal = getRandomValue(detail);
                request.getBody().put(detail.getKey(), randomVal);
            });
        }
        return request;
    }

    private Object getRandomValue(RandomKeyDetails details) {
        String type = details.getType();
        int length = details.getMaxLength();
        Random random = new Random();
        Object val = null;
        byte[] array;
        switch (type) {
            case "STRING":
                array = new byte[length];
                random.nextBytes(array);
                val = new String(array, StandardCharsets.UTF_8);
                break;
            case "EMAIL":
                val = UUID.randomUUID().toString() + "_load@gmail.com";
        }

        return val;
    }
}
