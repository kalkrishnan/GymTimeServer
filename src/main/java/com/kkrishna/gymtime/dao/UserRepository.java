package com.kkrishna.gymtime.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.RequestMapping;

@RepositoryRestResource(collectionResourceRel = "user", path = "user", excerptProjection = UserInlineFavorites.class)
public interface UserRepository extends CrudRepository<User, String> {

	
	User findByEmailAndPassword(@Param("email") String email, @Param("password") String password);

}
