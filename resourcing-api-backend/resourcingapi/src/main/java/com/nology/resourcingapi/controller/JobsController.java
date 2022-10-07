package com.nology.resourcingapi.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nology.resourcingapi.dto.JobCreateDTO;
import com.nology.resourcingapi.dto.JobUpdateDTO;
import com.nology.resourcingapi.entity.Job;
import com.nology.resourcingapi.exception.ResourceNotFoundException;
import com.nology.resourcingapi.service.JobService;

@RestController
@RequestMapping(value = "/jobs")
public class JobsController {
	
	@Autowired 
	private JobService jobService;
	
	// GET /jobs -- Fetch all jobs
	@GetMapping
	public ResponseEntity<List<Job>> getJobs() {
		List<Job> jobs = jobService.getAllJobs();
		
		return new ResponseEntity<>(jobs, HttpStatus.OK);
	}
	
	// GET /jobs/{id} -- (id, name, startDate, endDate, temp_id)
	@GetMapping("/{id}")
	public ResponseEntity<Job> getJobById(@PathVariable(value = "id") Long id) {
		Job job = jobService.getJob(id)
				.orElseThrow(() -> new ResourceNotFoundException("Not found Job with id " + id));
		
		return new ResponseEntity<>(job, HttpStatus.OK);
	}
	
	@PostMapping
	public ResponseEntity<Object> saveJob(@Valid @RequestBody JobCreateDTO job) {
		ResponseEntity<Object> jobCreated = jobService.create(job);
		
		return jobCreated;
//		return new ResponseEntity<>(job, HttpStatus.CREATED);
	}
	
	
	@PatchMapping("/{id}")
	public ResponseEntity<Object> updateJob(@PathVariable("id") long id, @Valid @RequestBody JobUpdateDTO jobUpdateRequest) {
		// save mergedJob to jobRepo
		ResponseEntity<Object> updatedJob =jobService.partiallyUpdateJob(id, jobUpdateRequest);
		return updatedJob;
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<HttpStatus> deleteJob(@PathVariable("id") long id) {
		jobService.deleteJob(id);
		
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	@GetMapping("/search")
	public ResponseEntity <List<Job>> searchJobsAssigned(@RequestParam("assigned")  boolean query) {
		List<Job> jobsAssigned = jobService.searchJobsAssigned(query);
		
		return ResponseEntity.ok(jobsAssigned);
	}
}
