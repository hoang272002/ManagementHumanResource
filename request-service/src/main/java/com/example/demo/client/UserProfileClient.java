package com.example.demo.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-profile-service")
public interface UserProfileClient {

    @GetMapping("/profiles/{userId}/isAdmin")
    Boolean isAdmin(@PathVariable("userId") String userId);
}