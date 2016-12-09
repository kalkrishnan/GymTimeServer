package com.kkrishna.gymtime.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.joda.time.DateTime;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

@Transactional
public interface CheckInRepository
		extends PagingAndSortingRepository<CheckIn, String>, JpaSpecificationExecutor<CheckIn> {
	Long countByGymId(String gymId);

	@Query("select hour(c.checkInTime), count(*) as traffic from CheckIn c where c.gymId=:gymId and c.checkInTime between :startTime and :endTime group by hour(c.checkInTime)")
	List<Object[]> countTraffic(@Param("gymId") String gymId, @Param("startTime") DateTime startTime,
			@Param("endTime") DateTime endTime);

}
