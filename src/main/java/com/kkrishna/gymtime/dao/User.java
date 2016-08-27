package com.kkrishna.gymtime.dao;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

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

}
