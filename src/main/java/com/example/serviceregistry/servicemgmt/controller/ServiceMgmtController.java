package com.example.serviceregistry.servicemgmt.controller;

import com.example.serviceregistry.servicemgmt.model.ServiceRequest;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ServiceMgmtController {

    private StringRedisTemplate redisTemplate;

    public ServiceMgmtController(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }


    @PostMapping("/api/service")
    public ResponseEntity registerService(@RequestBody ServiceRequest serviceRequest) {
        ListOperations<String, String> listOperations = redisTemplate.opsForList();
        listOperations.leftPush(serviceRequest.getName(), serviceRequest.getIp());

        return new ResponseEntity("등록이 완료되었습니다", HttpStatus.OK);
    }

    @GetMapping("/api/service")
    public ResponseEntity discoverService(@RequestParam String name) {
        ListOperations<String, String> listOperations = redisTemplate.opsForList();
        String serviceIp = listOperations.rightPopAndLeftPush(name, name);

        return new ResponseEntity(serviceIp, HttpStatus.OK);
    }

    @DeleteMapping("/api/service")
    public ResponseEntity deleteEntity(@RequestBody ServiceRequest serviceRequest) {
        ListOperations<String, String> listOperations = redisTemplate.opsForList();
        Long idx = listOperations.indexOf(serviceRequest.getName(), serviceRequest.getIp());
        listOperations.remove(serviceRequest.getName(), 1, serviceRequest.getIp());

        return new ResponseEntity("삭제가 완료되었습니다", HttpStatus.OK);
    }
}
