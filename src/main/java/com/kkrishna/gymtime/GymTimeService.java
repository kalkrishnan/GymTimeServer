package com.kkrishna.gymtime;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.hibernate.SessionFactory;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kkrishna.gymtime.common.CalendarApi;
import com.kkrishna.gymtime.common.GymStrategy;
import com.kkrishna.gymtime.common.GymStrategyGenerator;
import com.kkrishna.gymtime.dao.CheckInRepository;
import com.kkrishna.gymtime.dao.Gym;
import com.kkrishna.gymtime.dao.GymRepository;
import com.kkrishna.gymtime.dao.User;
import com.kkrishna.gymtime.dao.UserRepository;

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

	Gson gson = new GsonBuilder().create();
	com.google.api.services.calendar.Calendar service = CalendarApi.getCalendarService();

	@Autowired
	private SessionFactory sessionFactory;

	@RequestMapping("/searchGyms")
	public List<Gym> searchGyms(@RequestParam(value = "location") String location) {
		Gson gson = new GsonBuilder().create();
		GymStrategy strategy = gymStrategyGenerator.getGymStrategy();
		return strategy.searchGyms(location);
	}

	@RequestMapping("/searchGymsByLatLong")
	public List<Gym> searchGyms(@RequestParam(value = "latlong") String latLong,
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

	@RequestMapping("/addToFavorites")
	public void addToFavorites(@RequestBody User user) {

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

}
