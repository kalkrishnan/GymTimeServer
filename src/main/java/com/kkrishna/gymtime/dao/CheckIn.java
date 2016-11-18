package com.kkrishna.gymtime.dao;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;

import lombok.Builder;
import lombok.experimental.Tolerate;

@Builder
@Entity
public class CheckIn {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long Id;
	@Expose
	private String gymId;
	@Expose
	private String userId;
	@Expose
	private String traffic;
	@Expose
	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	private DateTime checkInTime;

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

	public DateTime getCheckInTime() {
		return checkInTime;
	}

	@Override
	public String toString() {
		return new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create().toJson(this);
	}

}
