package com.example.controller;

import com.example.model.UserProfile;
import com.example.repository.UserProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/profiles")
public class UserProfileController {

    @Autowired
    private UserProfileRepository userProfileRepository;
    
    @GetMapping("/{id}")
    public Optional<UserProfile> getUserProfile(@PathVariable String id) {
        return userProfileRepository.findById(id);
    }

    @PutMapping("/{id}")
    public UserProfile updateUserProfile(@PathVariable String id, @RequestBody UserProfile userProfile) {
        userProfile.setId(id);
        return userProfileRepository.save(userProfile);
    }
    
    @PostMapping(value = "/create")
    public UserProfile createProfile(@RequestBody UserProfile userProfile) {
    	return userProfileRepository.save(userProfile);
    }
    
    @GetMapping("/{userId}/isAdmin")
    public Boolean isAdmin(@PathVariable String userId) {
        UserProfile userProfile = userProfileRepository.findById(userId).orElse(null);
        return userProfile != null && userProfile.getAdmin();
    }
    
    @GetMapping(value = "/getAll")
	List<UserProfile> getAllProfile(){
    	return userProfileRepository.findAll();
    }
    
    @PostMapping("/Login")
    public Optional<UserProfile> login(@RequestBody UserProfile userProfile){
    	 Optional<UserProfile> foundProfile = userProfileRepository.findByUsernameAndPassword(userProfile.getUsername(),userProfile.getPassword());
    	 if (foundProfile.isPresent() && foundProfile.get().getPassword().equals(userProfile.getPassword())) {
    	        return foundProfile; 
    	    } else {
    	        throw new RuntimeException("Invalid email or password"); 
    	    }
    }
}