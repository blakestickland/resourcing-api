package com.nology.temp;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nology.job.Job;

public interface TempRepository extends JpaRepository<Temp, Long> {	
	
	List<Temp> findByFirstNameContaining(String firstName);
	
	Optional<Job> findByFirstName(String firstName);
	
}
