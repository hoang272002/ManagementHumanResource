package com.example.demo.service;

import com.example.demo.client.UserProfileClient;
import com.example.demo.model.Request;
import com.example.demo.repository.RequestRepository;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RequestService {


    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private UserProfileClient userProfileClient;

    @Autowired
    private NotificationService notificationService;
    
    @Autowired
	private AmqpTemplate amqpTemplate;
    
    @Value("${jsa.rabbitmq.exchange}")
	private String exchange;
    // Method to create a new request
    public String createRequest(String userId, String requestDetails) {
        Request request = new Request(userId, requestDetails,2);
        requestRepository.save(request);
        amqpTemplate.convertAndSend(exchange, "create", requestDetails);
        return "Request created successfully.";
    }

    // Method to approve a request if the user is an admin
    public String approveRequest(String userId, String requestId) {
        Boolean isAdmin = userProfileClient.isAdmin(userId);
        if (Boolean.TRUE.equals(isAdmin)) {
            Request request = requestRepository.findById(requestId).orElseThrow(() -> new RuntimeException("Request not found"));
            request.setApproved(1);
            requestRepository.save(request);
            amqpTemplate.convertAndSend("requests-exchange", "approve", requestId);
            return "Request approved successfully.";
        } else {
            return "You are not authorized to approve requests.";
        }
    }
    
    public String rejectRequest(String userId, String requestId) {
        Boolean isAdmin = userProfileClient.isAdmin(userId);
        if (Boolean.TRUE.equals(isAdmin)) {
            Request request = requestRepository.findById(requestId).orElseThrow(() -> new RuntimeException("Request not found"));
            request.setApproved(0);
            requestRepository.save(request);
            amqpTemplate.convertAndSend("requests-exchange", "reject", requestId);
            return "Request reject successfully.";
        } else {
            return "You are not authorized to approve requests.";
        }
    }

    // Method to retrieve all requests made by a certain user
    public List<Request> getAllRequestsByUser(String userId) {
        return requestRepository.findByUserId(userId);
    }
    
    @RabbitListener(queues = "${jsa.rabbitmq.queue}")
    public void receiveMessage(String request) {
        // Process the request here if needed
        System.out.println("Received Request: " + request);
        
        // Notify admin
        String subject = "New Request Received";
        String text = "A new request has been received with details: " + request;
        notifyAdmins(subject, text);
    }
    
    private void notifyAdmins(String subject, String text) {
        String adminEmails = "nptn1611@gmail.com"; // This should ideally be fetched from a configuration or database
        notificationService.sendNotification(adminEmails, subject, text);
    }
    
    public List<Request> getAllRequests() {
        return requestRepository.findAll(); // Fetch all requests from the database
    }
}