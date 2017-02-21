package com.kkrishna.gymtime.dao;

import org.springframework.data.repository.CrudRepository;

//@RestResource(exported = false)
public interface GymRepository extends CrudRepository<Gym, String> {

}
