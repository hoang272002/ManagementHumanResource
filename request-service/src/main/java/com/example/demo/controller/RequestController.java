package com.example.demo.controller;

import com.example.demo.model.Request;
import com.example.demo.service.RequestService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/requests")
public class RequestController {

    @Autowired
    private RequestService requestService;

    @PostMapping("/create")
    public ResponseEntity<String> createRequest(@RequestHeader("UserId") String userId, @RequestBody String requestDetails) {
        try {
            String response = requestService.createRequest(userId, requestDetails);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // Log the error and return a meaningful response
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }

    @PostMapping("/approve/{requestId}")
    public String approveRequest(@RequestHeader("UserId") String userId, @PathVariable String requestId) {
        return requestService.approveRequest(userId, requestId);
    }
    
    @PostMapping("/reject/{requestId}")
    public String rejectRequest(@RequestHeader("UserId") String userId, @PathVariable String requestId) {
        return requestService.rejectRequest(userId, requestId);
    }
    
    @GetMapping("/user/{userId}")
    public List<Request> getAllRequestsByUser(@PathVariable String userId) {
        return requestService.getAllRequestsByUser(userId);
    }
    
    @GetMapping("/all")
    public List<Request> getAllRequests() {
        return requestService.getAllRequests();
    }
}