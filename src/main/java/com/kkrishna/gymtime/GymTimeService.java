package com.kkrishna.gymtime;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;
import org.hibernate.SessionFactory;
import org.joda.time.DateTime;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kkrishna.gymtime.common.CalendarApi;
import com.kkrishna.gymtime.common.GymStrategy;
import com.kkrishna.gymtime.common.GymStrategyGenerator;
import com.kkrishna.gymtime.dao.CheckInRepository;
import com.kkrishna.gymtime.dao.Gym;
import com.kkrishna.gymtime.dao.GymRepository;
import com.kkrishna.gymtime.dao.User;
import com.kkrishna.gymtime.dao.UserRepository;
import com.kkrishna.gymtime.util.GymTimeHttpClient;

@RestController
public class GymTimeService {

	@Autowired
	private GymStrategyGenerator gymStrategyGenerator;

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Autowired
	CheckInRepository checkInRepo;

	@Autowired
	GymRepository gymRepo;

	@Autowired
	UserRepository userRepo;
	
	@Autowired
	private GymTimeHttpClient httpClient;
	
	@Value("${gymtime.googlegeocodeurl}")
	String geocodeUrl;

	Gson gson = new GsonBuilder().create();
	com.google.api.services.calendar.Calendar service = CalendarApi.getCalendarService();

	@Autowired
	private SessionFactory sessionFactory;

	@RequestMapping("/searchGyms")
	public List<Gym> searchGyms(@RequestParam(value = "location") String location,
			@RequestParam(value = "searchDistance") int searchDistance,
			@RequestParam(value = "keyword") String keyword,
			@RequestParam(value = "type") String type) throws ClientProtocolException, IOException {
		Gson gson = new GsonBuilder().create();
		GymStrategy strategy = gymStrategyGenerator.getGymStrategy();
		String url = geocodeUrl+location+"&sensor=false";
		String latLong = getLatLongFromZip(httpClient.getResponse(url));
		return strategy.searchGyms(latLong, searchDistance*1600, keyword, type);
	}

	private String getLatLongFromZip(String response) {
		System.out.println(response);
		JsonElement jelement = new JsonParser().parse(response);
		JsonObject jobject = jelement.getAsJsonObject();
		JsonArray jarray = jobject.getAsJsonArray("results");
		JsonElement location = jarray.get(0).getAsJsonObject().get("geometry").getAsJsonObject().get("location");
		return location.getAsJsonObject().get("lat").getAsString()+","+location.getAsJsonObject().get("lng").getAsString();
	}

	@RequestMapping("/searchGymsByLatLong")
	public List<Gym> searchGymsByLatLong(@RequestParam(value = "latlong") String latLong,
			@RequestParam(value = "radius") String radius) {
		Gson gson = new GsonBuilder().create();
		GymStrategy strategy = gymStrategyGenerator.getGymStrategy();
		return strategy.searchGyms(latLong, radius);
	}

	// @RequestMapping("/checkIn")
	// public void checkIn(@RequestParam(value = "checkInTime") String
	// checkInTime,
	// @RequestParam(value = "userId") String userId, @RequestParam(value =
	// "gymId") String gymId,
	// @RequestParam(value = "gymName") String gymName, @RequestParam(value =
	// "traffic") String traffic) {
	// // List the next 10 events from the primary calendar.
	// Event event = new Event().setLocation(gymName).setDescription("Checking
	// into " + gymName);
	//
	// org.joda.time.DateTime startDateTime = new
	// org.joda.time.DateTime(checkInTime);
	// checkInRepo.save(
	// CheckIn.builder().checkInTime(startDateTime).gymId(gymId).userId(userId).traffic(traffic).build());
	// Gym gym = gymRepo.findOne(gymId);
	// Calendar checkInDate = Calendar.getInstance();
	// checkInDate.setTimeInMillis(startDateTime.getMillis());
	// System.out.println("Test:*********************"+Integer.toString(new
	// org.joda.time.DateTime().getHourOfDay()));
	// gymRepo.save(Gym.builder().address(gym.getAddress()).name(gym.getName()).latLong(gym.getLatLong())
	// .traffic(updateGymTraffic(gym, checkInDate)).build());
	// }

	@RequestMapping("/updateUser")
	public void updateUser(@RequestBody User user) {

		userRepo.save(user).toString();
	}

	@Async
	public Map<Integer, Integer> updateGymTraffic(Gym gym, Calendar checkInDate) {

		Map<Integer, Integer> traffic = gym.getTraffic();
		int hour = checkInDate.get(Calendar.HOUR_OF_DAY);
		traffic.put(hour, traffic.get(hour) + 1);
		traffic.put(hour + 1, traffic.get(hour + 1) + 1);
		return traffic;
	}

	@RequestMapping("/getGymTraffic")
	public List<Object[]> getGymTraffic(@RequestParam(value = "gymId") String gymId) {

		// Session session;
		// try {
		// session = sessionFactory.getCurrentSession();
		// } catch (HibernateException e) {
		// session = sessionFactory.openSession();
		// }
		// Criteria criteria = session.createCriteria(CheckIn.class);
		// ProjectionList projectionList = Projections.projectionList();
		// criteria.add(Restrictions.between("checkInTime", new
		// DateTime().withTimeAtStartOfDay(),
		// new DateTime().plusDays(1).withTimeAtStartOfDay()));
		// projectionList.add(Projections.sqlGroupProjection("count(*) as
		// traffic, hour(checkInTime)",
		// "hour(checkInTime), month(checkInTime), year(checkInTime)", new
		// String[] { "traffic", "hour" },
		// new Type[] { IntegerType.INSTANCE, IntegerType.INSTANCE }));
		// criteria.setProjection(projectionList);
		DateTime startTime = new DateTime().withTimeAtStartOfDay();
		System.out.println(startTime.toString());
		DateTime endTime = new DateTime().plusDays(1).withTimeAtStartOfDay();
		System.out.println(endTime.toString());

		return checkInRepo.countTraffic(gymId, startTime, endTime);
	}

	@RequestMapping("/parseResults")
	public List<String> parseResults() throws IOException {

		String url = "http://www.google.com/search?q=uci%20arc";
		System.out.println("Fetching : " + url + "\n\n");
		List<String> urls = new ArrayList<String>();
		Document doc = Jsoup.connect(url).userAgent("Mozilla").get();

		Elements div = doc.body().select("*");

		for (Element di : div) {

			urls.add("\nLink : " + di.html() + "");

		}
		return urls;
	}

}
