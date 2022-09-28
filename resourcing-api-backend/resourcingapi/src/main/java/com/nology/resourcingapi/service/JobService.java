package com.nology.resourcingapi.service;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

import com.nology.resourcingapi.dto.JobCreateDTO;
import com.nology.resourcingapi.dto.JobDTO;
import com.nology.resourcingapi.dto.JobUpdateDTO;
import com.nology.resourcingapi.dto.TempCreateDTO;
import com.nology.resourcingapi.entity.Job;
import com.nology.resourcingapi.entity.Temp;
import com.nology.resourcingapi.exception.ResourceNotFoundException;
import com.nology.resourcingapi.repository.JobRepository;
import com.nology.resourcingapi.repository.TempRepository;

@Service
@Transactional
public class JobService {
	@Autowired
	private JobRepository jobRepository;
	@Autowired
	private TempRepository tempRepository;
	
	public List<Job> getAllJobs() {
		return jobRepository.findAll();
	}
	
	public Optional<Job> getJob(@PathVariable long id) {
		return jobRepository.findById(id);
	}
	
	public ResponseEntity<Object> create(@Valid JobCreateDTO job) {
		Job dbJob = new Job(job.getName(), job.getStartDate(), job.getEndDate());
		Job savedJob = jobRepository.save(dbJob);
		if (jobRepository.findById(savedJob.getId()).isPresent()) {
			return ResponseEntity.accepted().body("Successfully Created Job");
		} else 
			return ResponseEntity.unprocessableEntity().body("Failed to Create specified Job");
	}
	
	public Job update(Job exisitingJob) {
		Job dbJob  = new Job(exisitingJob.getName(), exisitingJob.getStartDate(), exisitingJob.getEndDate(), exisitingJob.getTemp());
		return jobRepository.save(dbJob);
	}
	
	public void deleteJob(@PathVariable long id) {
		jobRepository.findById(id).orElseThrow(() -> 
			new ResourceNotFoundException("Job not found with id :" + id));
		
		jobRepository.deleteById(id);;
	}
	
	public Job partiallyUpdateJob(@PathVariable long id, JobUpdateDTO jobUpdateRequest) {
		// check whether the job with given id exists in the DB
		Job exisitingJob = getJob(id)
				.orElseThrow(() -> new ResourceNotFoundException("Job with id: " + id + "not found"));
		// Loop through fields to get field keys
		// if jobRequest contains same key as dbJob, set dbJob value to jobRequest value
		if ((jobUpdateRequest.getName()) != null)
			exisitingJob.setName(jobUpdateRequest.getName());
		if ((jobUpdateRequest.getStartDate()) != null)
			exisitingJob.setStartDate(jobUpdateRequest.getStartDate());
		if ((jobUpdateRequest.getEndDate()) != null)
			exisitingJob.setEndDate(jobUpdateRequest.getEndDate());
		if ((jobUpdateRequest.getTempId()) != null) {
			long newTempId = jobUpdateRequest.getTempId();
			Temp temp = tempRepository.findById(newTempId).orElseThrow(() -> 
				new ResourceNotFoundException("Temp not found with id :" + id));
			exisitingJob.setTemp(temp);
		}
//				job.setAssigned(jobRequest.isAssigned());
		Job updatedJob = jobRepository.save(exisitingJob);
		
		return updatedJob;
	}

}

