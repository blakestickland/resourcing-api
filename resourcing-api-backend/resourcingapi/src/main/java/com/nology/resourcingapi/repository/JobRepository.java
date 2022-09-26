package com.nology.resourcingapi.repository;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nology.resourcingapi.entity.Job;
import com.nology.resourcingapi.entity.Temp;

public interface JobRepository extends JpaRepository<Job, Long> {
//	List<Job> findByTempId(Long tempId);
//	
//	List<Job> findByAssigned(boolean assigned);
//	
//	@Transactional
//	void deleteByTempId(long tempId);
	
	Optional<Temp> findByName(String name);
}
