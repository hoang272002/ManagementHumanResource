package com.example.demo;

import java.util.List;
import java.util.Optional;

import org.springframework.cloud.openfeign.FeignClient;

import org.springframework.web.bind.annotation.*;

import com.example.model.UserProfile;

@FeignClient(name = "user-profile-service")
public interface QuanLyProfileClient {
	
	@PostMapping(value = "/profiles/create")
	UserProfile createProfile(@RequestBody UserProfile userProfile);
	
	@GetMapping(value = "/profiles/getAll")
	List<UserProfile> getAllProfile();
	
	 @PutMapping("/profiles/{id}")
	 UserProfile updateUserProfile(@PathVariable String id, @RequestBody UserProfile userProfile) ;
	 
	 @DeleteMapping("/profiles/delete/{id}")
	 void deleteProfile(@PathVariable String id);
	 
	 @PostMapping("/profiles/Login")
	 public Optional<UserProfile> login(@RequestBody UserProfile userProfile);
	 
	 @GetMapping("/profiles/{id}")
	 public Optional<UserProfile> getUserProfile(@PathVariable String id);
}