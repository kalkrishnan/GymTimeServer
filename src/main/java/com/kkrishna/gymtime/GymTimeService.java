package com.kkrishna.gymtime;

import java.util.Date;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
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

		String createTableScript = "CREATE TABLE IF NOT EXISTS GYM_" + gymId
				+ "(TRAFFIC VARCHAR(255), EXPIRY TIMESTAMP)";
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

	@GET
	@Path("/signup")
	@Produces("application/json")
	public Response signup(@QueryParam("name") String name, @QueryParam("email") String email,
			@QueryParam("password") String password) {

		createUserTable();
		System.out.println(email);
		String selectEmailScript = "SELECT count(*) FROM USER WHERE USER.EMAIL='" + email + "'";
		int exists = jdbcTemplate.queryForObject(selectEmailScript, Integer.class);
		if (exists > 0)
			return Response.status(200).header("Access-Control-Allow-Origin", "*").entity("Email already exists")
					.build();

		String insertTableScript = "INSERT INTO USER VALUES ('" + name + "','" + email + "','" + password + "');";
		jdbcTemplate.execute(insertTableScript);

		return Response.status(200).header("Access-Control-Allow-Origin", "*").entity(email).build();
	}

	private void createUserTable() {
		
		String createTableScript = "CREATE TABLE IF NOT EXISTS USER (NAME VARCHAR(255), EMAIL VARCHAR(255), PASSWORD VARCHAR(255));";

		jdbcTemplate.execute(createTableScript);
	}

	@GET
	@Path("/login")
	@Produces("application/json")
	public Response login(@QueryParam("email") String email, @QueryParam("password") String password) {
		try
		{
			System.out.println(email);
		createUserTable();
		String selectPasswordScript = "SELECT PASSWORD FROM USER WHERE EMAIL='" + email+"'";
		String _password = jdbcTemplate.queryForObject(selectPasswordScript, String.class);
		if (password.equalsIgnoreCase(_password))
			return Response.status(200).header("Access-Control-Allow-Origin", "*").build();
		else
		{
			return Response.status(400).header("Access-Control-Allow-Origin", "*").entity("Login Failed: Invalid Email or Password")
					.build();
		}
		}catch(DataAccessException e)
		{
			System.out.println("invalid exception");
			
		}
		System.out.println("invalid exception 2");
		return Response.status(400).header("Access-Control-Allow-Origin", "*").entity("Login Failed: Invalid Email or Password")
				.build();
	}

}
