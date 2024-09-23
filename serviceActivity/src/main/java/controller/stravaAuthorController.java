package controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import model.ActivityDetail;
import model.StravaAuthor;
import repository.stravaAuthorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@RestController	
@RequestMapping("/api/Strava")
public class stravaAuthorController {
	@Autowired
	stravaAuthorRepository repo;
	
	private static final String TOKEN_URL = "https://www.strava.com/api/v3/oauth/token";
	 private static final String STRAVA_ACTIVITIES_URL = "https://www.strava.com/api/v3/athlete/activities";
	 private static final Logger logger = LoggerFactory.getLogger(stravaAuthorController.class);
	@PostMapping("/create")
	public ResponseEntity<StravaAuthor> CreateStravaAuthor(@RequestBody StravaAuthor stra) {
		try {
			  	RestTemplate restTemplate = new RestTemplate();
		        HttpHeaders headers = new HttpHeaders();
		        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

		        // Prepare the request body parameters
		        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
		        body.add("client_id", stra.getClientID());
		        body.add("client_secret", stra.getClientSecret());
		        body.add("grant_type", "authorization_code");
		        body.add("code", stra.getCode());

		        // Create the request entity
		        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(body, headers);

		        // Make the POST request
		        ResponseEntity<String> apiResponse = restTemplate.exchange(TOKEN_URL, HttpMethod.POST, requestEntity, String.class);


		        // Parse the response
		        ObjectMapper mapper = new ObjectMapper();
		        JsonNode responseJson = mapper.readTree(apiResponse.getBody());

		        // Extract the refresh_token from the response
		        String newRefreshToken = responseJson.path("refresh_token").asText();
		        
		        stra.setRefesh(newRefreshToken);
				StravaAuthor _author = repo.save(new StravaAuthor(stra.getManv(), stra.getClientID(),stra.getClientSecret(),stra.getRefesh()));
				return new ResponseEntity<>(_author, HttpStatus.CREATED);
		} catch (Exception e) {
			stra.setManv(e.getMessage());
			return new ResponseEntity<>(stra, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	 @GetMapping("/getActivity")
	    public ResponseEntity<?> getActivity() {
	        try {
	            List<StravaAuthor> stra = repo.findAll();
	            List<ActivityDetail> activityDetails = new ArrayList<>();

	            for (StravaAuthor person : stra) {
	                // Set up the request body and headers
	                RestTemplate restTemplate = new RestTemplate();
	                HttpHeaders headers = new HttpHeaders();
	                headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

	                // Prepare the request body parameters as MultiValueMap
	                MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
	                body.add("client_id", person.getClientID());
	                body.add("client_secret", person.getClientSecret());
	                body.add("grant_type", "refresh_token");
	                body.add("refresh_token", person.getRefesh());

	                // Create the request entity with headers and body
	                HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(body, headers);

	                // Make the POST request
	                ResponseEntity<String> apiResponse = restTemplate.exchange(TOKEN_URL, HttpMethod.POST, requestEntity, String.class);
	                ObjectMapper mapper = new ObjectMapper();
	                JsonNode responseJson = mapper.readTree(apiResponse.getBody());

	                // Extract the access_token from the response
	                String accessToken = responseJson.path("access_token").asText();
	                if (accessToken.isEmpty()) {
	                    logger.error("Access token not found in the response");
	                    return new ResponseEntity<>("Access token not found", HttpStatus.BAD_REQUEST);
	                }

	                HttpHeaders headers1 = new HttpHeaders();
	                headers1.set("Authorization", "Bearer " + accessToken);

	                // Create the request entity with headers
	                HttpEntity<String> requestEntity1 = new HttpEntity<>(headers1);
	                ResponseEntity<String> apiResponse1 = restTemplate.exchange(STRAVA_ACTIVITIES_URL, HttpMethod.GET, requestEntity1, String.class);

	                // Parse the response
	                JsonNode activities = mapper.readTree(apiResponse1.getBody());

	                double totalDistance = 0;
	                String startTime = null;
	                String endTime = null;

	                for (JsonNode activity : activities) {
	                    double distance = activity.path("distance").asDouble() / 1000; // Convert meters to kilometers
	                    String movingTime = activity.path("moving_time").asText(); // Moving time in seconds
	                    String activityStartTime = activity.path("start_date").asText(); // Start time of the activity

	                    totalDistance += distance;

	                    if (startTime == null || activityStartTime.compareTo(startTime) < 0) {
	                        startTime = activityStartTime;
	                    }
	                    if (endTime == null || activityStartTime.compareTo(endTime) > 0) {
	                        endTime = activityStartTime;
	                    }

	                    activityDetails.add(new ActivityDetail(
	                        person.getManv(),
	                        distance,
	                        movingTime,
	                        totalDistance,
	                        startTime,
	                        endTime
	                    ));
	                }
	            }

	            return new ResponseEntity<>(activityDetails, HttpStatus.OK);
	        } catch (RestClientException e) {
	            logger.error("Error during API call: ", e);
	            return new ResponseEntity<>("Error during API call: " + e.getMessage(), HttpStatus.SERVICE_UNAVAILABLE);
	        } catch (JsonProcessingException e) {
	            logger.error("Error processing JSON: ", e);
	            return new ResponseEntity<>("Error processing JSON: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	        } catch (Exception e) {
	            logger.error("Unexpected error: ", e);
	            return new ResponseEntity<>("Unexpected error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	        }
	    }
	 
	 @GetMapping("/getActivity/{userid}")
	 public ResponseEntity<?> getActivityByUserId(@PathVariable String userid) {
	     try {
	         // Tìm người dùng Strava dựa trên userid
	         Optional<StravaAuthor> optionalPerson = repo.findById(userid);
	         if (!optionalPerson.isPresent()) {
	             return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
	         }

	         StravaAuthor person = optionalPerson.get();
	         RestTemplate restTemplate = new RestTemplate();

	         // Thiết lập body và header của yêu cầu
	         HttpHeaders headers = new HttpHeaders();
	         headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

	         MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
	         body.add("client_id", person.getClientID());
	         body.add("client_secret", person.getClientSecret());
	         body.add("grant_type", "refresh_token");
	         body.add("refresh_token", person.getRefesh());

	         HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(body, headers);

	         // Gửi yêu cầu POST để lấy access_token
	         ResponseEntity<String> apiResponse = restTemplate.exchange(TOKEN_URL, HttpMethod.POST, requestEntity, String.class);
	         ObjectMapper mapper = new ObjectMapper();
	         JsonNode responseJson = mapper.readTree(apiResponse.getBody());

	         // Lấy access_token từ phản hồi
	         String accessToken = responseJson.path("access_token").asText();
	         if (accessToken.isEmpty()) {
	             logger.error("Access token not found in the response");
	             return new ResponseEntity<>("Access token not found", HttpStatus.BAD_REQUEST);
	         }

	         // Thiết lập header với access_token
	         HttpHeaders headers1 = new HttpHeaders();
	         headers1.set("Authorization", "Bearer " + accessToken);

	         HttpEntity<String> requestEntity1 = new HttpEntity<>(headers1);
	         ResponseEntity<String> apiResponse1 = restTemplate.exchange(STRAVA_ACTIVITIES_URL, HttpMethod.GET, requestEntity1, String.class);

	         // Xử lý phản hồi từ API Strava
	         JsonNode activities = mapper.readTree(apiResponse1.getBody());

	         double totalDistance = 0;
	         String startTime = null;
	         String endTime = null;
	         List<ActivityDetail> activityDetails = new ArrayList<>();

	         for (JsonNode activity : activities) {
	             double distance = activity.path("distance").asDouble() / 1000; // Đổi từ mét sang km
	             String movingTime = activity.path("moving_time").asText(); // Thời gian di chuyển
	             String activityStartTime = activity.path("start_date").asText(); // Thời gian bắt đầu hoạt động

	             totalDistance += distance;

	             if (startTime == null || activityStartTime.compareTo(startTime) < 0) {
	                 startTime = activityStartTime;
	             }
	             if (endTime == null || activityStartTime.compareTo(endTime) > 0) {
	                 endTime = activityStartTime;
	             }

	             activityDetails.add(new ActivityDetail(
	                 person.getManv(),
	                 distance,
	                 movingTime,
	                 totalDistance,
	                 startTime,
	                 endTime
	             ));
	         }

	         return new ResponseEntity<>(activityDetails, HttpStatus.OK);
	     } catch (RestClientException e) {
	         logger.error("Error during API call: ", e);
	         return new ResponseEntity<>("Error during API call: " + e.getMessage(), HttpStatus.SERVICE_UNAVAILABLE);
	     } catch (JsonProcessingException e) {
	         logger.error("Error processing JSON: ", e);
	         return new ResponseEntity<>("Error processing JSON: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	     } catch (Exception e) {
	         logger.error("Unexpected error: ", e);
	         return new ResponseEntity<>("Unexpected error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	     }
	 }


	
	
}
