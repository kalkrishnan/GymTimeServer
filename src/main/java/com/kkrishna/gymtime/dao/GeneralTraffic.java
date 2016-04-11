package com.kkrishna.gymtime.dao;

public class GeneralTraffic implements Traffic {

	private double trafficStrength;
	
	public GeneralTraffic(double trafficStrength) {

		this.trafficStrength = trafficStrength;
	}
	public double getHowHeavyTrafficIs() {
		return this.trafficStrength;
	}

}
