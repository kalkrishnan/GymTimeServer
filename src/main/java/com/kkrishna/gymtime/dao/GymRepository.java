package com.kkrishna.gymtime.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RestResource;

//@RestResource(exported = false)
public interface GymRepository extends CrudRepository<Gym, String> {

}
