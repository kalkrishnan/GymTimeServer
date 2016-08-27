package com.kkrishna.gymtime.dao;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface CheckInRepository extends PagingAndSortingRepository<CheckIn, String> {
	Long countByGymId(String gymId);

}
