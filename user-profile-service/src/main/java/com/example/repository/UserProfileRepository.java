package com.example.repository;

import com.example.model.UserProfile;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserProfileRepository extends MongoRepository<UserProfile, String> {

	Optional<UserProfile> findByUsernameAndPassword(String username, String password);
}