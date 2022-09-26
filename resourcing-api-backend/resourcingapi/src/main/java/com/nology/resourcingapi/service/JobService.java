package com.nology.resourcingapi.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

import com.nology.resourcingapi.dto.JobDTO;
import com.nology.resourcingapi.entity.Job;
import com.nology.resourcingapi.exception.ResourceNotFoundException;
import com.nology.resourcingapi.repository.JobRepository;

@Service
@Transactional
public class JobService {
	@Autowired
	private JobRepository jobRepository;
	
	public List<Job> all() {
		return jobRepository.findAll();
	}
	
	public Optional<Job> getJob(@PathVariable long id) {
		return jobRepository.findById(id);
	}
	
	public void create(JobDTO job) {
		Job dbJob  = new Job(job.getName(), job.getStartDate(), job.getEndDate());
		jobRepository.save(dbJob);
	}
	
	public void deleteJob(@PathVariable long id) {
		jobRepository.findById(id).orElseThrow(() -> 
			new ResourceNotFoundException("Job not found with id :" + id));
		
		jobRepository.deleteById(id);;
	}

}