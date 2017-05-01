package com.mydevgeek;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class RestTemplateServiceApplication {

	public static void main(String[] args) throws IOException{

		SpringApplication.run(RestTemplateServiceApplication.class, args);
		getRequest();
		getRequestPassParameters();
		postRequest();
		putRequest();
		deleteRequest();
		System.out.println("-----");
		postExchange();
		getResponseEntity();

	}

	private static void getRequest() {
		String url = "http://localhost:8080/user/1";

		RestTemplate restTemplate = new RestTemplate();
		User user = restTemplate.getForObject(url, User.class);

		System.out.println("GET User :" + user.getFirstName() + " " + user.getLastName());
	}

	private static void getRequestPassParameters() {
		String url = "http://localhost:8080/user/{id}";

		Map<String, String> params = new HashMap<>();
		params.put("id", "1");

		RestTemplate restTemplate = new RestTemplate();
		User user = restTemplate.getForObject(url, User.class, params);

		System.out.println("GET User :" + user.getFirstName() + " " + user.getLastName());

	}

	private static void postRequest() {
		String url = "http://localhost:8080/user";

		User user = new User();
		user.setId(1L);
		user.setFirstName("John");
		user.setLastName("Doe");

		RestTemplate restTemplate = new RestTemplate();
		User result = restTemplate.postForObject(url, user, User.class);
		System.out.println(result.getFirstName() + " " + result.getLastName());
	}

	private static void putRequest() {
		String url = "http://localhost:8080/user/{id}";

		Map<String, String> params = new HashMap<>();
		params.put("id", "1");

		User user = new User();
		user.setId(1L);
		user.setFirstName("John");
		user.setLastName("Smith");

		RestTemplate restTemplate = new RestTemplate();
		restTemplate.put(url, user, params);
	}

	private static void deleteRequest() {
		String url = "http://localhost:8080/user/{id}";

		Map<String, String> params = new HashMap<>();
		params.put("id", "1");

		RestTemplate restTemplate = new RestTemplate();
		restTemplate.delete(url, params);


	}

	private static void postExchange() {
		String url = "http://localhost:8080/user";
		RestTemplate restTemplate = new RestTemplate();

		User user = new User();
		user.setId(1L);
		user.setFirstName("John");
		user.setLastName("Smith");

		HttpEntity<User> request = new HttpEntity<>(user);
		ResponseEntity<User> response = restTemplate
				.exchange(url, HttpMethod.POST, request, User.class);

		System.out.println(response.getStatusCode());
		System.out.println(response.getBody());
	}

	private static void postExchangeWithCallback() {
		String url = "http://localhost:8080/user";
		AsyncRestTemplate restTemplate = new AsyncRestTemplate();

		User user = new User();
		user.setId(1L);
		user.setFirstName("John");
		user.setLastName("Smith");

		HttpEntity<User> request = new HttpEntity<>(user);
		ListenableFuture<ResponseEntity<String>> response = restTemplate
				.exchange(url, HttpMethod.POST, request, String.class);
		response.addCallback(new ListenableFutureCallback<ResponseEntity<String>>() {
			@Override
			public void onFailure(Throwable throwable) {
				//do something
			}

			@Override
			public void onSuccess(ResponseEntity<String> response) {
				System.out.println(response.getBody());
			}
		});


	}

	private static void getResponseEntity() throws IOException {

		String url = "http://localhost:8080/user/1";

		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders httpHeaders = restTemplate.headForHeaders(url);

		ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
		System.out.println(response);

		ObjectMapper objectMapper = new ObjectMapper();
		User user = objectMapper.readValue(response.getBody(), User.class);
		System.out.println(httpHeaders.getContentType());
		System.out.println(response.getStatusCode());
		System.out.println(user.getFirstName() + " " + user.getLastName());
	}

}
