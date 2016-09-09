//package com.kkrishna.gymtime;
//
//import static org.junit.Assert.assertTrue;
//
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.boot.test.IntegrationTest;
//import org.springframework.boot.test.SpringApplicationConfiguration;
//import org.springframework.boot.test.TestRestTemplate;
//import org.springframework.http.ResponseEntity;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//import org.springframework.test.context.web.WebAppConfiguration;
//import org.springframework.web.client.RestTemplate;
//
//import com.google.gson.Gson;
//import com.google.gson.GsonBuilder;
//import com.kkrishna.gymtime.dao.Gym;
//
//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringApplicationConfiguration(classes = GymTimeApplication.class)
//@WebAppConfiguration
//@IntegrationTest("server.port=9000")
//public class GymTimeServiceTest {
//
//	private RestTemplate restTemplate = new TestRestTemplate();
//
//	@Test
//	public void gymTime() {
//		ResponseEntity<String> entity = restTemplate.getForEntity("http://localhost:9000/gymTimeService", String.class);
//		System.out.println(entity.getBody());
//		assertTrue(entity.getStatusCode().is2xxSuccessful());
//		assertTrue(entity.getBody().equals("GymTime: Up and Running!"));
//
//	}
//
//	@Test
//	public void searchGyms() {
//		ResponseEntity<String> entity = restTemplate
//				.getForEntity("http://localhost:9000/gymTimeService/searchGyms?location=92617", String.class);
//		System.out.println(entity.getBody());
//		Gson gson = new GsonBuilder().create();
//		Gym gym = gson.fromJson(entity.getBody(), Gym.class);
//		assertTrue(entity.getStatusCode().is2xxSuccessful());
//		assertTrue(gym.getAddress().equals("680 California Ave, Irvine, CA 92617, United States"));
//
//	}
//}