package com.kkrishna.gymtime;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventAttendee;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.calendar.model.EventReminder;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kkrishna.gymtime.common.CalendarApi;
import com.kkrishna.gymtime.common.GymStrategy;
import com.kkrishna.gymtime.common.GymStrategyGenerator;
import com.kkrishna.gymtime.dao.CheckIn;
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
	com.google.api.services.calendar.Calendar service = CalendarApi.getCalendarService();

	@RequestMapping("/searchGyms")
	public List<Gym> searchGyms(@RequestParam(value = "location") String location) {
		Gson gson = new GsonBuilder().create();
		GymStrategy strategy = gymStrategyGenerator.getGymStrategy();
		return strategy.searchGyms(location);
	}

	@RequestMapping("/searchGymsByLatLong")
	public List<Gym> searchGyms(@RequestParam(value = "latlong") String latLong, @RequestParam(value = "radius") String radius) {
		Gson gson = new GsonBuilder().create();
		GymStrategy strategy = gymStrategyGenerator.getGymStrategy();
		return strategy.searchGyms(latLong, radius);
	}

	@RequestMapping("/checkIn")
	public void checkIn(@RequestParam(value = "checkInTime") String checkInTime,
			@RequestParam(value = "userId") String userId, @RequestParam(value = "gymId") String gymId,
			@RequestParam(value = "gymName") String gymName, @RequestParam(value = "traffic") String traffic) {
		// List the next 10 events from the primary calendar.
		Event event = new Event().setLocation(gymName).setDescription("Checking into " + gymName);

		DateTime startDateTime = new DateTime(checkInTime);
		EventDateTime start = new EventDateTime().setDateTime(startDateTime);
		event.setStart(start);
		EventAttendee[] attendees = new EventAttendee[] { new EventAttendee().setEmail(userId), };
		event.setAttendees(Arrays.asList(attendees));

		EventReminder[] reminderOverrides = new EventReminder[] {
				new EventReminder().setMethod("email").setMinutes(60) };
		Event.Reminders reminders = new Event.Reminders().setUseDefault(false)
				.setOverrides(Arrays.asList(reminderOverrides));
		event.setReminders(reminders);

		String calendarId = "primary";
		try {
			event = service.events().insert(calendarId, event).execute();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		checkInRepo.save(CheckIn.builder().checkInTime(new org.joda.time.DateTime(checkInTime)).gymId(gymId)
				.userId(userId).traffic(traffic).build());

	}

}
