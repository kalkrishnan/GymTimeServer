package com.kkrishna.gymtime;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kkrishna.gymtime.common.GymStrategy;
import com.kkrishna.gymtime.common.GymStrategyGenerator;
import com.kkrishna.gymtime.common.HTTPMessages;
import com.kkrishna.gymtime.dao.Gym;

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
	public Response checkIn(@QueryParam("gym") String gymId, @QueryParam("gym") String userId,
			@QueryParam("traffic") String traffic) {

		String createTableScript = "CREATE TABLE IF NOT EXISTS GYM_" + gymId
				+ "(USER_ID VARCHAR(255), TRAFFIC VARCHAR(255), EXPIRY TIMESTAMP)";
		jdbcTemplate.execute(createTableScript);
		Date expiryDate = new Date(System.currentTimeMillis() + 60 * 60 * 1000);
		String insertTableScript = "INSERT INTO GYM_" + gymId + "(TRAFFIC, EXPIRY) VALUES ('" + userId + "','" + traffic
				+ "','" + new java.sql.Timestamp(expiryDate.getTime()) + "');";
		jdbcTemplate.execute(insertTableScript);
		String countScript = "SELECT count(*) FROM GYM_" + gymId + " WHERE EXPIRY > '"
				+ new java.sql.Timestamp((new Date()).getTime()) + "'";
		int count = jdbcTemplate.queryForObject(countScript, Integer.class);
		return Response.status(200).header("Access-Control-Allow-Origin", "*").entity(count).build();
	}

	@GET
	@Path("/addToFavorites")
	@Produces("application/json")
	public Response addToFavorites(@QueryParam("gymId") String gymId, @QueryParam("userId") String userId) {

		System.out.println(GymTimeService.class.getName() + "  Gym Id: " + gymId + " User Id: " + userId);
		createFavoritesTable();
		String insertTableScript = "INSERT INTO USER_FAVORITES VALUES ('" + gymId + "','" + userId + "');";
		jdbcTemplate.execute(insertTableScript);
		return Response.status(200).header("Access-Control-Allow-Origin", "*").entity(HTTPMessages.SUCCESS.getMessage())
				.build();
	}

	private void createFavoritesTable() {
		String createTableScript = "CREATE TABLE IF NOT EXISTS USER_FAVORITES(GYM_ID VARCHAR(255), USER_ID VARCHAR(255))";
		jdbcTemplate.execute(createTableScript);
	}

	@GET
	@Path("/removeFromFavorites")
	@Produces("application/json")
	public Response removeFromFavorites(@QueryParam("gymId") String gymId, @QueryParam("userId") String userId) {

		String deleteTableScript = "DELETE FROM USER_FAVORITES WHERE GYM_ID='" + gymId + "' AND USER_ID='" + userId
				+ "';";
		jdbcTemplate.execute(deleteTableScript);
		return Response.status(200).header("Access-Control-Allow-Origin", "*").entity(HTTPMessages.SUCCESS.getMessage())
				.build();
	}

	@SuppressWarnings("unchecked")
	@GET
	@Path("/getUserFavorites")
	@Produces("application/json")
	public Response getUserFavorites(@QueryParam("userId") String userId) {
		createFavoritesTable();
		try {
			System.out.print("User Id: " + userId);
			String countScript = "SELECT GYM_ID FROM USER_FAVORITES WHERE USER_ID='" + userId + "'";
			List<Map<String, Object>> gymIds = jdbcTemplate.queryForList(countScript);

			Gson gson = new GsonBuilder().create();
			GymStrategy strategy = gymStrategyGenerator.getGymStrategy();
			List<Gym> favorites = new ArrayList<Gym>();
			for (Map gymId : gymIds) {
				System.out.print("Gym Id: " + (String) gymId.get("GYM_ID"));
				favorites.addAll(strategy.searchGyms((String) gymId.get("GYM_ID")));
			}
			String output = gson.toJson(favorites);
			return Response.status(200).header("Access-Control-Allow-Origin", "*").entity(output).build();
		} catch (EmptyResultDataAccessException e) {
			return Response.status(200).header("Access-Control-Allow-Origin", "*").build();

		}

	}

	@GET
	@Path("/signup")
	@Produces("application/json")
	public Response signup(@QueryParam("name") String name, @QueryParam("email") String email,
			@QueryParam("password") String password) {

		createUserTable();
		String selectEmailScript = "SELECT count(*) FROM USER WHERE USER.EMAIL='" + email + "'";
		int exists = jdbcTemplate.queryForObject(selectEmailScript, Integer.class);
		if (exists > 0) {
			System.out.println("Email Already exists");
			return Response.status(200).header("Access-Control-Allow-Origin", "*")
					.entity(HTTPMessages.EMAIL_ALREADY_EXISTS.getMessage()).build();
		}

		UUID userId = java.util.UUID.randomUUID();
		String insertTableScript = "INSERT INTO USER VALUES ('" + userId + "','" + name + "','" + email + "','"
				+ password + "');";
		jdbcTemplate.execute(insertTableScript);

		return Response.status(200).header("Access-Control-Allow-Origin", "*").entity(userId).build();
	}

	@GET
	@Path("/login")
	@Produces("application/json")
	public Response login(@QueryParam("email") String email, @QueryParam("password") String password) {
		try {
			createUserTable();
			String selectPasswordScript = "SELECT PASSWORD FROM USER WHERE EMAIL='" + email + "'";
			String _password = jdbcTemplate.queryForObject(selectPasswordScript, String.class);
			if (password.equalsIgnoreCase(_password)) {
				String userIdSelectScript = "SELECT USER_ID FROM USER WHERE EMAIL='" + email + "'";
				String userId = jdbcTemplate.queryForObject(userIdSelectScript, String.class);

				return Response.status(200).header("Access-Control-Allow-Origin", "*").entity(userId).build();
			} else {
				return Response.status(200).header("Access-Control-Allow-Origin", "*")
						.entity(HTTPMessages.INVALID_EMAIL_OR_PASSWORD.getMessage()).build();
			}
		} catch (EmptyResultDataAccessException e) {
			System.out.println("invalid exception");
			return Response.status(200).header("Access-Control-Allow-Origin", "*")
					.entity(HTTPMessages.INVALID_EMAIL_OR_PASSWORD.getMessage()).build();

		}

	}

	private void createUserTable() {

		String createTableScript = "CREATE TABLE IF NOT EXISTS USER (USER_ID VARCHAR(255) PRIMARY KEY, NAME VARCHAR(255), EMAIL VARCHAR(255), PASSWORD VARCHAR(255));";

		jdbcTemplate.execute(createTableScript);
	}
	
	@GET
	@Path("/addComment")
	@Produces("application/json")
	public Response addComment(@QueryParam("gymId") String gymId, @QueryParam("userId") String userId, @QueryParam("comment") String comment, @QueryParam("timestamp") String time) {

		System.out.println(GymTimeService.class.getName() + "  Gym Id: " + gymId + " User Id: " + userId);
		createCommentsTable();
		String insertTableScript = "INSERT INTO COMMENTS VALUES ('" + gymId + "','" + userId  + "','" + comment + "','" + time+"');";
		jdbcTemplate.execute(insertTableScript);
		return Response.status(200).header("Access-Control-Allow-Origin", "*").entity(HTTPMessages.SUCCESS.getMessage())
				.build();
	}

	private void createCommentsTable() {
		String createTableScript = "CREATE TABLE IF NOT EXISTS COMMENTS(GYM_ID VARCHAR(255), USER_ID VARCHAR(255), COMMENT VARCHAR(255), TIME VARCHAR(255))";
		jdbcTemplate.execute(createTableScript);
	}

}
