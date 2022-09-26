package com.nology.resourcingapi.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nology.resourcingapi.entity.Job;
import com.nology.resourcingapi.entity.Temp;

public interface TempRepository extends JpaRepository<Temp, Long> {	
//	List<Temp> findByJobId(Long jobId);
	
//	List<Temp> findByFirstNameContaining(String firstName);
	Optional<Job> findByFirstName(String firstName);
}
