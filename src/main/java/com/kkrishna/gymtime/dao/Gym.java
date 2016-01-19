package com.kkrishna.gymtime.dao;

import java.util.Collections;
import java.util.List;

public class Gym {

	private final String id;
	private final String name;
	private final String address;
	private final List<Traffic> traffic;

	public Gym(String id, String name, String address, List<Traffic> traffic) {
		this.id = id;
		this.name = name;
		this.address = address;
		this.traffic = Collections.unmodifiableList(traffic);
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getAddress() {
		return address;
	}

	public List<Traffic> getTraffic() {
		return traffic;
	}

}
