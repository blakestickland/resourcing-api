package com.nology.job;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.nology.temp.Temp;

public interface JobRepository extends JpaRepository<Job, Long> {
	List<Job> findByTempId(Long tempId);
//	
//	List<Job> findByAssigned(boolean assigned);
//	
//	@Transactional
//	void deleteByTempId(long tempId);
	
	Optional<Temp> findByName(String name);
	
	@Modifying
	@Query("update Job u set u.temp = :temp where u.id = :id")
	void updateTemp(@Param(value = "id") long id, @Param(value = "temp") Temp temp);
	
	
	@Query ("SELECT j FROM Job j WHERE " + 
			"j.name LIKE CONCAT('%', :query, '%')")
	List<Job> searchJobsSQL(String query);
	
	@Query ("SELECT j FROM Job j WHERE " + 
			"j.temp IS NULL " + 
			"ORDER BY j.id")
	List<Job> searchJobsAssignedNullSQL();
	
	@Query ("SELECT j FROM Job j WHERE " +
			"j.temp IS NOT NULL " +  
			"ORDER BY j.id")
	List<Job> searchJobsAssignedNotNullSQL();
	
	@Query(value = "SELECT * FROM jobs j WHERE " +
	        "(j.start_date BETWEEN ?1 AND ?2) AND " + 
	        "(j.end_date BETWEEN ?1 AND ?2) AND " +
	        "(j.temp_id = ?3)", nativeQuery = true)
	List<Job> findByDateBetween(Date startDate, Date endDate, Long incomingTempId);
	
	@Query(value = "SELECT * FROM jobs j WHERE " +
            "(j.start_date BETWEEN ?1 AND ?2) AND " + 
            "(j.temp_id = ?3)", nativeQuery = true)
    List<Job> findByStartDateBetween(Date incomingStartDate, Date incomingEndDate, Long incomingTempId);
	
	@Query(value = "SELECT * FROM jobs j WHERE " +
            "(j.end_date BETWEEN ?1 AND ?2) AND " + 
            "(j.temp_id = ?3)", nativeQuery = true)
    List<Job> findByEndDateBetween(Date incomingStartDate, Date incomingEndDate, Long incomingTempId);
}
