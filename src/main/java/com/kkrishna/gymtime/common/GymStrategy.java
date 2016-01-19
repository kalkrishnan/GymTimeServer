package com.kkrishna.gymtime.common;

import com.kkrishna.gymtime.dao.Gym;

public interface GymStrategy {

	public Gym searchGyms(String location);
}
