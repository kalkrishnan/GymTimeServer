package com.kkrishna.gymtime.dao;

import java.util.List;

import org.springframework.data.rest.core.config.Projection;

@Projection(name = "inlineFavorites", types = { User.class })
public interface UserInlineFavorites {

	public String getName() ;

	public String getEmail();

	public String getPassword() ;

	public List<Gym> getFavorites() ;
}
