package com.kkrishna.gymtime.common;

import java.util.List;

import com.kkrishna.gymtime.dao.Gym;

public interface GymStrategy {

	public List<Gym> searchGyms(String location, int radius, String keyword, String type);

	public List<Gym> searchGyms(String latLong, String radius);
}
