package com.nology.resourcingapi.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import com.nology.resourcingapi.entity.Temp;

public interface TempRepository extends JpaRepository<Temp, Long> {	
//	List<Temp> findByJobId(Long jobId);
	
//	List<Temp> findByFirstNameContaining(String firstName);
}
