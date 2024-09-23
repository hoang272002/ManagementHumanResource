package repository;


import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import model.DoiThuong;



public interface DoiThuongRepository extends MongoRepository<DoiThuong, String> {
	
	Optional<DoiThuong> findByManvAndIdGift(String manv, String idGift);
	List<DoiThuong> findByManv(String manv);
}
