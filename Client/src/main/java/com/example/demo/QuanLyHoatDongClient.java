package com.example.demo;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "serviceActivity")
public interface QuanLyHoatDongClient {
	@GetMapping("/api/Strava/getActivity")
	ResponseEntity<?> getActivity();
	
	@GetMapping("/api/Strava/getActivity/{userid}")
	ResponseEntity<?> getActivityByUserId(@PathVariable String userid);
	
	
}
