package com.kkrishna.gymtime.dao;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.google.gson.GsonBuilder;

import lombok.Builder;
import lombok.experimental.Tolerate;

@Entity
@Builder
public class User {

	private String name;
	@Id
	private String email;
	private String password;
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private List<Gym> favorites;
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private List<Alert> alerts;

	@Tolerate
	User() {

	}

	public String getName() {
		return name;
	}

	public String getEmail() {
		return email;
	}

	public String getPassword() {
		return password;
	}

	public List<Gym> getFavorites() {
		return favorites;
	}
	
	public List<Alert> getAlerts() {
		return alerts;
	}

	@Override
	public String toString() {
		return new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create().toJson(this);
	}
}
