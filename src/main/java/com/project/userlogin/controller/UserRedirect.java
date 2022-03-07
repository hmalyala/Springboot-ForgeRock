package com.project.userlogin.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class UserRedirect {
	
	@GetMapping(path="/userredirect")
	public String forgerRockAuthenticationUsingRedirect(@RequestBody Map body) throws JsonMappingException, JsonProcessingException {
		
//		body = json.loads(request.getb);
		System.out.println(body);
		
		String code = (String) body.get("code");
				
		RestTemplate restTemplate = new RestTemplate();

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.add("PRIVATE-TOKEN", "xyz");
		
		MultiValueMap<String, String> bodyPair = new LinkedMultiValueMap();
		bodyPair.add("client_id", "1");
		bodyPair.add("redirect_uri", "Spring Boot 101");
		bodyPair.add("code", code);
		bodyPair.add("grant_type", "authorization_code");
		bodyPair.add("access_token", "123");
		bodyPair.add("client_secret", "123");

		HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(bodyPair, headers);

		ResponseEntity<String> response =
		    restTemplate.exchange("http://localhost:8080/trial",
		                          HttpMethod.POST,
		                          entity,
		                          String.class);
		
		ObjectMapper mapper = new ObjectMapper();
		Map<String, ArrayList<String>> map = mapper.readValue(response.getBody(), Map.class);		
		
		System.out.println(response.getHeaders());		
		System.out.println(response.getStatusCode());		

		
		headers = new HttpHeaders();
		headers.set("Authorization", "Bearer "+map.get("access_token").get(0));
		
		entity = new HttpEntity<>(headers);

		response =
		    restTemplate.exchange("http://localhost:8080/dummy",
		                          HttpMethod.POST,
		                          entity,
		                          String.class);
		
		return "Hi";
	}
	
	@GetMapping(path="/")
	public String returnValue() {
		return "Hi";
	}
	
	@PostMapping(path="/trial")
	public MultiValueMap trail(@RequestHeader HttpHeaders headers,@RequestParam MultiValueMap paramMap) {
		System.out.println(headers);
		return paramMap;
	}
	
	@PostMapping(path="/dummy")
	public String dummy(@RequestHeader HttpHeaders headers) {
		System.out.println("in dummy");
		System.out.println(headers);
		return "";
	}
	
}
