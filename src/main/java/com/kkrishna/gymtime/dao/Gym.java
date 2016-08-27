package com.kkrishna.gymtime.dao;

import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Builder;
import lombok.experimental.Tolerate;

@Builder
@Entity
public class Gym {

	@Id
	private String latLong;
	private String name;
	private String address;
	@ElementCollection(targetClass = Double.class)
	private List<Double> traffic;

	@Tolerate
	Gym() {
	}

	public String getName() {
		return name;
	}

	public String getAddress() {
		return address;
	}

	public List<Double> getTraffic() {
		return traffic;
	}

	public String getLatLong() {
		return latLong;
	}

}
