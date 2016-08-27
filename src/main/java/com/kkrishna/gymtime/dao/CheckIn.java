package com.kkrishna.gymtime.dao;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Builder;
import lombok.experimental.Tolerate;

@Builder
@Entity
public class CheckIn {

	@Id
	private String gymId;
	private String userId;
	private String traffic;
	private Timestamp checkInTime;

	@Tolerate
	CheckIn() {

	}

	public String getUserId() {
		return userId;
	}

	public String getTraffic() {
		return traffic;
	}

	public String getGymId() {
		return gymId;
	}

	public Timestamp getCheckInTime() {
		return checkInTime;
	}

}
