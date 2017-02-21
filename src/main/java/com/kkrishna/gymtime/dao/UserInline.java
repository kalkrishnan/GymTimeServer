package com.kkrishna.gymtime.dao;

import java.util.List;

import org.springframework.data.rest.core.config.Projection;

@Projection(name = "userInline", types = { User.class })
public interface UserInline {

	public String getName() ;

	public String getEmail();

	public String getPassword() ;

	public List<Gym> getFavorites() ;
	
	public List<Alert> getAlerts() ;
}
