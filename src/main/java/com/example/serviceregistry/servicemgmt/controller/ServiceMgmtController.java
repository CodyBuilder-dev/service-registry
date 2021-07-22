package com.example.serviceregistry.servicemgmt.controller;

import com.example.serviceregistry.servicemgmt.model.ServiceRequest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ServiceMgmtController {

    StringRedisTemplate redisTemplate;

    public ServiceMgmtController(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }


    @PostMapping("/api/service")
    public ResponseEntity registerService(@RequestBody ServiceRequest serviceRequest) {
        ValueOperations<String, String> stringOpertaion = redisTemplate.opsForValue();

        stringOpertaion.set(serviceRequest.getName(), serviceRequest.getIp());
        String savedIp = stringOpertaion.get(serviceRequest.getName());
        return new ResponseEntity("등록이 완료되었습니다", HttpStatus.OK);
    }

    @GetMapping("/api/service")
    public ResponseEntity discoverService(@RequestParam String name) {
        ValueOperations<String, String> stringOpertaion = redisTemplate.opsForValue();

        return new ResponseEntity(stringOpertaion.get(name), HttpStatus.OK);
    }

}
