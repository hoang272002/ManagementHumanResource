package com.example.demo;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import model.DiemThuong;
import model.DoiThuong;

@FeignClient(name = "service")
public interface QuanLyDiemThuongClient {
	@GetMapping("/api/DiemThuong/getAll")
	ResponseEntity<List<DiemThuong>> XemDanhSachDiem();
	
	@PutMapping("/api/DiemThuong/CongDiem/{id}")
	ResponseEntity<DiemThuong> CongDiemNhanVien(@PathVariable String id, @RequestBody DiemThuong DT) ;
	
	@GetMapping("/api/DiemThuong/getByID/{id}")
	ResponseEntity<DiemThuong> xemDiemCua1Profile(@PathVariable String id);
	
	@GetMapping("/api/DiemThuong/GetListGift")
	ResponseEntity<?> getListGift();
	
	@PostMapping("/api/DiemThuong/DoiThuong/{idnv}")
	ResponseEntity<?> DoiThuong(@PathVariable String idnv, @RequestBody DoiThuong DT);
	
	@GetMapping("/api/DiemThuong/DoiThuong/getByManv/{id}")
	List<DoiThuong> XemDoiDiem(@PathVariable String id);
}
