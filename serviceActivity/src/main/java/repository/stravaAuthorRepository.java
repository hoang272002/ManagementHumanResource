package repository;


import org.springframework.data.mongodb.repository.MongoRepository;

import model.StravaAuthor;



public interface stravaAuthorRepository extends MongoRepository<StravaAuthor, String> {

}

