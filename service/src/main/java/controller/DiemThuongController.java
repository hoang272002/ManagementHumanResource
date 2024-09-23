package controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import model.DiemThuong;
import model.DoiThuong;
import repository.DiemThuongRepository;
import repository.DoiThuongRepository;


@RestController
@RequestMapping("/api/DiemThuong")
public class DiemThuongController {
	@Autowired
	DiemThuongRepository repo;
	
	@Autowired
	DoiThuongRepository repoDoi;
	
	@Autowired
	private Environment env;
	
	private static final String API_URL = "https://private-251d8-urboxapi.apiary-mock.com/2.0/gift/getlist";
	
	@PostMapping("/create")
	public ResponseEntity<DiemThuong> CreateDiemThuong(@RequestBody DiemThuong DT) {
		try {
			DiemThuong _diemthuong = repo.save(new DiemThuong(DT.getManv(), DT.getDiemThuong()));
			return new ResponseEntity<>(_diemthuong, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/DoiThuong/getByManv/{id}")
	public List<DoiThuong> XemDoiDiem(@PathVariable String id) {
		return repoDoi.findByManv(id);
	}
	
	@GetMapping("/getAll")
	public ResponseEntity<List<DiemThuong>> XemDanhSachDiem() {
		try {
			List<DiemThuong> diemThuonglst = new ArrayList<DiemThuong>();
			
			repo.findAll().forEach(diemThuonglst::add);
			if (diemThuonglst.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
			
			return new ResponseEntity<>(diemThuonglst, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/getByID/{id}")
	public ResponseEntity<DiemThuong> xemDiemCua1Profile(@PathVariable String id) {
	    try {
	   
	        Optional<DiemThuong> profileData = repo.findById(id);

	        
	        if (profileData.isPresent()) {
	            return new ResponseEntity<>(profileData.get(), HttpStatus.OK);
	        } else {
	         
	            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	        }
	    } catch (Exception e) {
	 
	        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}
	
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<HttpStatus> Xoa(@PathVariable String id) {
		try {
			 repo.deleteById(id);
			 return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		 } catch (Exception e) {
			 return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		 }
	}
	
	@PutMapping("/CongDiem/{id}")
	public ResponseEntity<DiemThuong> CongDiemNhanVien(@PathVariable String id, @RequestBody DiemThuong DT) {
		Optional<DiemThuong> DTData = repo.findById(id);

		if (DTData.isPresent()) {
			DiemThuong _diemthuong = DTData.get();
			
			int updDiem = _diemthuong.getDiemThuong() + DT.getDiemThuong();
			_diemthuong.setDiemThuong(updDiem);
			
			return new ResponseEntity<>(repo.save(_diemthuong), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@PutMapping("/TruDiem/{id}")
	public ResponseEntity<DiemThuong> TruDiemNhanVien(@PathVariable String id, @RequestBody DiemThuong DT) {
		Optional<DiemThuong> DTData = repo.findById(id);

		if (DTData.isPresent()) {
			DiemThuong _diemthuong = DTData.get();
			
			int updDiem = _diemthuong.getDiemThuong() - DT.getDiemThuong();
			_diemthuong.setDiemThuong(updDiem);
			
			return new ResponseEntity<>(repo.save(_diemthuong), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@GetMapping("/GetListGift")
	public ResponseEntity<?> getListGift() {
		try {
            // Make the external API call
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> apiResponse = restTemplate.getForEntity(API_URL, String.class);

            // Parse the API response body as JSON
            ObjectMapper mapper = new ObjectMapper();
            JsonNode listGift = mapper.readTree(apiResponse.getBody());

           
            return new ResponseEntity<>(listGift, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
	}
	
	@PostMapping("/DoiThuong/{idnv}")
	public ResponseEntity<?> DoiThuong(@PathVariable String idnv, @RequestBody DoiThuong DT) {
		try {
			 Optional<DoiThuong> existingDoiThuong = repoDoi.findByManvAndIdGift(idnv, DT.getIdGift());
			 Optional<DiemThuong> existingDiemThuong = repo.findById(idnv);
			 
			DiemThuong data = existingDiemThuong.get();
			if (existingDoiThuong.isPresent()) 
			{
				return new ResponseEntity<>("idgift already exists, cannot save.", HttpStatus.CONFLICT);
	        }
			else {
				if(data.getDiemThuong() >= DT.getPoint()) {
					DT.setManv(idnv);
	                
	                // Create RestTemplate instance
	                RestTemplate restTemplate = new RestTemplate();
	                
	                String serverPort = env.getProperty("server.port");

	                // Prepare the URL for the TruDiemNhanVien API call
	                String truDiemUrl = "http://localhost:" + serverPort + "/api/DiemThuong/TruDiem/" + idnv;

	                // Create a request body with diemThuong value
	                DiemThuong diemThuongRequest = new DiemThuong();
	                diemThuongRequest.setDiemThuong(DT.getPoint());

	                // Make the API call to subtract points
	                ResponseEntity<DiemThuong> TruDiemNhanVien = restTemplate.exchange(
	                    truDiemUrl,
	                    HttpMethod.PUT,
	                    new HttpEntity<>(diemThuongRequest),
	                    DiemThuong.class
	                );
	                if (TruDiemNhanVien.getStatusCode() == HttpStatus.OK) {
	                	DoiThuong savedDoiThuong = repoDoi.save( new DoiThuong(DT.getManv() ,DT.getIdGift(),DT.getNameGift(),DT.getPoint() ));
	                    return new ResponseEntity<>(savedDoiThuong, HttpStatus.CREATED);
	                } else {
	                    // If the TruDiem API call fails, you might want to handle it differently
	                    return new ResponseEntity<>("Failed to subtract points", HttpStatus.INTERNAL_SERVER_ERROR);
	                }
				}
				else {
					return new ResponseEntity<>("Your point is not enough", HttpStatus.INTERNAL_SERVER_ERROR);
				}
			
			}
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
	}
}
