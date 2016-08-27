package com.kkrishna.gymtime.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class GymStrategyGenerator {

	@Value("${gymtime.infosource}")
	String gymInfoSource;

	@Autowired
	private GoogleApiGymStrategy googleApiStrategy;

	public GymStrategy getGymStrategy() {

		if (this.gymInfoSource.equals(GymInfoSources.GOOGLE.toString())) {
			return googleApiStrategy;
		}
		return null;
	}
}
