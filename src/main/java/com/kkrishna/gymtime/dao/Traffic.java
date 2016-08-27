package com.kkrishna.gymtime.dao;

import javax.persistence.Embeddable;

import lombok.Builder;
import lombok.experimental.Tolerate;

@Builder
@Embeddable
public class Traffic {

	private String type;
	private double trafficStrength;

	@Tolerate
	Traffic() {

	}

	public double getTrafficStrength() {
		return trafficStrength;
	}

	public String getType() {
		return type;
	}

}
