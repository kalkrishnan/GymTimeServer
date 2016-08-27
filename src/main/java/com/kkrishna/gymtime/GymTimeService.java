package com.kkrishna.gymtime;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kkrishna.gymtime.common.GymStrategy;
import com.kkrishna.gymtime.common.GymStrategyGenerator;
import com.kkrishna.gymtime.dao.CheckInRepository;
import com.kkrishna.gymtime.dao.Gym;

@RestController
public class GymTimeService {

	@Autowired
	private GymStrategyGenerator gymStrategyGenerator;

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Autowired
	CheckInRepository checkInRepo;

	Gson gson = new GsonBuilder().create();

	@RequestMapping("/searchGyms")
	public List<Gym> searchGyms(@RequestParam(value = "location") String location) {
		Gson gson = new GsonBuilder().create();
		GymStrategy strategy = gymStrategyGenerator.getGymStrategy();
		return strategy.searchGyms(location);
	}

}
