package com.kkrishna.gymtime.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface CommentRepository extends CrudRepository<Comment, String> {

	public List<Comment> findByGymId(@Param("gymId") String gymId);
}
