package com.kkrishna.gymtime.dao;

import java.util.Map;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;

import com.google.gson.GsonBuilder;

import lombok.Builder;
import lombok.experimental.Tolerate;

@Builder
@Entity
public class Gym {

	@Id
	private String latLong;
	private String name;
	private String address;
	@ElementCollection
	private Map<Integer, Integer> traffic;

	@Tolerate
	Gym() {
	}

	public String getName() {
		return name;
	}

	public String getAddress() {
		return address;
	}

	public Map<Integer, Integer> getTraffic() {
		return traffic;
	}

	public String getLatLong() {
		return latLong;
	}

}
