package com.kkrishna.gymtime;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

@ApplicationPath("/")
@Component
public class JerseyConfig extends ResourceConfig {

	public JerseyConfig() {
		register(GymTimeService.class);
	}

}
