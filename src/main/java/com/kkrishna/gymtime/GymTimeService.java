package com.kkrishna.gymtime;

import java.util.Date;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kkrishna.gymtime.common.GymStrategy;
import com.kkrishna.gymtime.common.GymStrategyGenerator;

@Component
@Path("/gymTimeService")
public class GymTimeService {

	@Autowired
	private GymStrategyGenerator gymStrategyGenerator;

	@Autowired
	JdbcTemplate jdbcTemplate;

	@GET
	@Produces("application/json")
	public String gymTimeService() {
		return "GymTime: Up and Running!";
	}

	@GET
	@Path("/searchGyms")
	@Produces("application/json")
	public Response searchGyms(@QueryParam("location") String location) {
		Gson gson = new GsonBuilder().create();
		GymStrategy strategy = gymStrategyGenerator.getGymStrategy();
		String output = gson.toJson(strategy.searchGyms(location));
		return Response.status(200).header("Access-Control-Allow-Origin", "*").entity(output).build();
	}

	@GET
	@Path("/checkIn")
	@Produces("application/json")
	public Response checkIn(@QueryParam("gym") String gymId, @QueryParam("traffic") String traffic) {

		String createTableScript = "CREATE TABLE IF NOT EXISTS GYM_" + gymId + "(TRAFFIC VARCHAR(255), EXPIRY TIMESTAMP)";
		jdbcTemplate.execute(createTableScript);
		Date expiryDate = new Date(System.currentTimeMillis() + 60 * 60 * 1000);
		String insertTableScript = "INSERT INTO GYM_" + gymId + "(TRAFFIC, EXPIRY) VALUES ('" + traffic + "','"
				+ new java.sql.Timestamp(expiryDate.getTime()) + "');";
		jdbcTemplate.execute(insertTableScript);
		String countScript = "SELECT count(*) FROM GYM_" + gymId + " WHERE EXPIRY > '"
				+ new java.sql.Timestamp((new Date()).getTime()) + "'";
		int count = jdbcTemplate.queryForObject(countScript, Integer.class);
		return Response.status(200).header("Access-Control-Allow-Origin", "*").entity(count).build();
	}
}
