package com.example.demo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.example.model.UserProfile;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import model.ActivityDetail;
import model.DiemThuong;
import model.DoiThuong;
import model.GiftDetail;
import model.GiftResponse;

@SpringBootApplication
@Controller
@EnableFeignClients


public class ClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(ClientApplication.class, args);}
		
		@Autowired
		QuanLyProfileClient qlProfile;
		
		@Autowired
		QuanLyHoatDongClient qlHD;
		
		@Autowired
		QuanLyDiemThuongClient qlPoint;
		
	
		@GetMapping("/QuanLy")
		public ModelAndView Home(@RequestParam String userid) {
		    ModelAndView modelAndView = new ModelAndView("quanly.html");
		    try {
		        // Load the profile list
		        List<UserProfile> ds = qlProfile.getAllProfile();

		        
		        ResponseEntity<?> response = qlHD.getActivity();
		        List<ActivityDetail> dsHD = new ArrayList<>();

		        if (response.getStatusCode().is2xxSuccessful()) {
		            ObjectMapper objectMapper = new ObjectMapper();
		            dsHD = objectMapper.convertValue(response.getBody(), new TypeReference<List<ActivityDetail>>(){});
		        }
		        
		        ResponseEntity<List<DiemThuong>> response1 =  qlPoint.XemDanhSachDiem();
		        List<DiemThuong> dsDiem = new ArrayList<>();
		        
		        if (response1.getStatusCode().is2xxSuccessful()) {
		            ObjectMapper objectMapper = new ObjectMapper();
		            dsDiem = objectMapper.convertValue(response1.getBody(), new TypeReference<List<DiemThuong>>(){});
		        }
		        // Pass both lists to the view
		        modelAndView.addObject("adminID", userid);
		        modelAndView.addObject("dsprofile", ds);
		        modelAndView.addObject("dshd", dsHD);
		        modelAndView.addObject("dspoint", dsDiem);
		    } catch (Exception e) {
		        // Log the error and return an error view or message
		        e.printStackTrace();
		        modelAndView.setViewName("error"); // or return an error page
		    }

		    return modelAndView;
		}
		
		@PostMapping("/QLProfile/Create")
		public ModelAndView create(@RequestParam String adminID, @RequestParam String id, @RequestParam String name,@RequestParam String cancuoc,@RequestParam String mst,@RequestParam String email,@RequestParam String address,@RequestParam String phone,@RequestParam String bank,@RequestParam String bank_name) {
			 UserProfile userProfile = new UserProfile();
			 userProfile.setId(id);
			 userProfile.setName(name);
			 userProfile.setCancuoc(cancuoc);
			 userProfile.setMst(mst);
			 userProfile.setEmail(email);
			 userProfile.setAddress(address);
			 userProfile.setPhone(phone);
			 userProfile.setBank(bank);
			 userProfile.setBank_name(bank_name);
			
			qlProfile.createProfile(userProfile);
			return new ModelAndView("redirect:/QuanLy?userid=" + adminID);
		}
		
		@PostMapping("/QLProfile/Update")
		public ModelAndView update(@RequestParam String adminID, @RequestParam String id, @RequestParam String name,@RequestParam String cancuoc,@RequestParam String mst,@RequestParam String email,@RequestParam String address,@RequestParam String phone,@RequestParam String bank,@RequestParam String bank_name) {
			 UserProfile userProfile = new UserProfile();
			 userProfile.setId(id);
			 userProfile.setName(name);
			 userProfile.setCancuoc(cancuoc);
			 userProfile.setMst(mst);
			 userProfile.setEmail(email);
			 userProfile.setAddress(address);
			 userProfile.setPhone(phone);
			 userProfile.setBank(bank);
			 userProfile.setBank_name(bank_name);
			
			qlProfile.updateUserProfile(id,userProfile);
			return new ModelAndView("redirect:/QuanLy?userid=" + adminID);
		}
		
		@PostMapping("/QLPoints/CongDiem")
		public ModelAndView congDiem(@RequestParam String adminID, @RequestParam String manv,@RequestParam int diemThuong) {
			 DiemThuong DT = new DiemThuong();
			 DT.setDiemThuong(diemThuong);
			 DT.setManv(manv);
			 
			qlPoint.CongDiemNhanVien(manv, DT);
			return new ModelAndView("redirect:/QuanLy?userid=" + adminID);
		}
		
		@GetMapping("/LoginForm")
		public ModelAndView loginForm() {
			return new ModelAndView("login.html");
		}
		
		@PostMapping("/NhanVien/Update")
		public ModelAndView updateProfileNV(@RequestParam String username, @RequestParam String password,@RequestParam String userid, @RequestParam String name, @RequestParam String cancuoc,@RequestParam String mst,@RequestParam String email,@RequestParam String address,@RequestParam String phone,@RequestParam String bank,@RequestParam String bank_name) {
			 UserProfile userProfile = new UserProfile();
			 userProfile.setId(userid);
			 userProfile.setName(name);
			 userProfile.setCancuoc(cancuoc);
			 userProfile.setMst(mst);
			 userProfile.setEmail(email);
			 userProfile.setAddress(address);
			 userProfile.setPhone(phone);
			 userProfile.setBank(bank);
			 userProfile.setBank_name(bank_name);
			 userProfile.setUsername(username);
			 userProfile.setPassword(password);
			
			qlProfile.updateUserProfile(userid,userProfile);
			return new ModelAndView("redirect:/NhanVien?userid=" + userid);
		}
		
		@GetMapping("/NhanVien")
		public ModelAndView empForm(@RequestParam String userid) {
		    ModelAndView modelAndView = new ModelAndView("nhanvien.html");
		    
		    if (userid != null) {
		        Optional<UserProfile> profile = qlProfile.getUserProfile(userid);
		        
		        ResponseEntity<DiemThuong> PointProfile = qlPoint.xemDiemCua1Profile(userid);
		        List<DoiThuong> doiThuong = qlPoint.XemDoiDiem(userid);
		       
		        ResponseEntity<?> response = qlPoint.getListGift();
		        GiftResponse giftResponse = null;

		        ObjectMapper objectMapper = new ObjectMapper();
		        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

		        if (response.getStatusCode().is2xxSuccessful()) {
		            if (response.getBody() != null) {
		                try {
		                    // Assuming response body is a LinkedHashMap
		                    if (response.getBody() instanceof LinkedHashMap) {
		                        String responseBodyJson = objectMapper.writeValueAsString(response.getBody());
		                        giftResponse = objectMapper.readValue(responseBodyJson, GiftResponse.class);
		                    } else {
		                        System.err.println("Unexpected response body type: " + response.getBody().getClass());
		                    }
		                } catch (IOException e) {
		                    System.err.println("Error parsing GiftResponse JSON: " + e.getMessage());
		                    e.printStackTrace(); // Print the stack trace for debugging
		                }
		            } else {
		                System.err.println("Response body is null.");
		            }
		        } else {
		            System.err.println("Failed to fetch gift details. HTTP status: " + response.getStatusCode());
		        }

		        // Extract list of items from GiftResponse
		        List<GiftDetail> giftDetails = new ArrayList<>();
		        if (giftResponse != null && giftResponse.getData() != null) {
		            for (GiftResponse.Data.Item item : giftResponse.getData().getItems()) {
		                GiftDetail giftDetail = new GiftDetail();
		                giftDetail.setId(item.getId());
		                giftDetail.setName(item.getName());
		                giftDetail.setBrandName(item.getBrand().getTitle());
		                giftDetail.setPrice(item.getDetail().get(0).getPrice()); // Assumes there's at least one detail
		                giftDetails.add(giftDetail);
		            }
		        } else {
		            System.err.println("GiftResponse or giftResponse data is null.");
		        }
		        
		        ResponseEntity<?> response1 = qlHD.getActivityByUserId(userid);
		        List<ActivityDetail> dsHD = new ArrayList<>();

		        if (response1.getStatusCode().is2xxSuccessful()) {
		            ObjectMapper objectMapper1 = new ObjectMapper();
		            dsHD = objectMapper1.convertValue(response1.getBody(), new TypeReference<List<ActivityDetail>>(){});
		        }
		    	
		    	
		        modelAndView.addObject("UserProfile", profile.get());
		        modelAndView.addObject("PointProfile", PointProfile.getBody());
		      modelAndView.addObject("giftDetails", giftDetails);
		      modelAndView.addObject("listDoiThuong", doiThuong);
		      modelAndView.addObject("listHD", dsHD);
		      
		        modelAndView.addObject("userid", userid);
		    }
		    
		    return modelAndView;
		}
		
		@PostMapping("/NhanVien/Doithuong")
		public ModelAndView empForm(@RequestParam String idnv, 
		                            @RequestParam String idgift, 
		                            @RequestParam String namegift, 
		                            @RequestParam String points) {
		    DoiThuong dt = new DoiThuong();
		    dt.setManv(idnv);
		    dt.setIdGift(idgift);
		    dt.setNameGift(namegift);

		    // Parse points safely
		    try {
		    	int pointValue = Integer.parseInt(points);
		        dt.setPoint(pointValue);
		    } catch (NumberFormatException e) {
		        System.err.println("Invalid number format for points: " + e.getMessage());
		       
		        
		    }
		    
		   qlPoint.DoiThuong(idnv, dt);
		   
		   
		    return new ModelAndView("redirect:/NhanVien?userid=" + idnv);
		}
		
		
		@PostMapping("/Login")
		public ModelAndView login(@RequestParam String username, @RequestParam String password) {
			UserProfile userProfile = new UserProfile();
			userProfile.setUsername(username);
			userProfile.setPassword(password);
			Optional<UserProfile> profile= qlProfile.login(userProfile);
			String userId = profile.get().getId();
			 if (profile.isPresent() && profile.get().getAdmin()) {
				 
			        return new ModelAndView("redirect:/QuanLy?userid=" + userId); 
			    } else {
			    	  
			         
			    	return new ModelAndView("redirect:/NhanVien?userid=" + userId); 
			    }
		}
		
}
