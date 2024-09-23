package com.example.demo.repository;

import com.example.demo.model.Request;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface RequestRepository extends MongoRepository<Request, String> {
    List<Request> findByUserId(String userId);
}