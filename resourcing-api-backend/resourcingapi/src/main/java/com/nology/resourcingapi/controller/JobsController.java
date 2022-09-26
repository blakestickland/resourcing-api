package com.nology.resourcingapi.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.nology.resourcingapi.dto.JobDTO;
import com.nology.resourcingapi.entity.Job;
import com.nology.resourcingapi.exception.ResourceNotFoundException;
import com.nology.resourcingapi.repository.JobRepository;
import com.nology.resourcingapi.service.JobService;
import com.nology.resourcingapi.service.TempService;

//@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping(value = "/jobs")
public class JobsController {
	
//	@Autowired
//	private TempService tempService;
	
	@Autowired 
	private JobService jobService;
	
	@Autowired
	private JobRepository jobRepository;
	
	// GET /jobs -- Fetch all jobs
	@GetMapping
	public List<Job> getJobs() {
		return jobService.all();
	}
	
	// GET /jobs/{id} -- (id, name, tempId, startDate, endDate)
	@GetMapping("/{id}")
	public ResponseEntity<Job> getJobById(@PathVariable(value = "id") Long id) {
		Job job = jobService.getJob(id)
				.orElseThrow(() -> new ResourceNotFoundException("Not found Job with id " + id));
		return new ResponseEntity<>(job, HttpStatus.OK);
	}
	
	@PostMapping
	@ResponseStatus(value = HttpStatus.CREATED)
	public void saveJob(@Valid @RequestBody JobDTO job) {
		jobService.create(job);
	}
//	
//	@PatchMapping("/{id}")
//	public ResponseEntity<Job> updateJob(@PathVariable("id") long id, @RequestBody Job jobRequest) {
//		Job job = jobService.getJob(id)
//				.orElseThrow(() -> new ResourceNotFoundException("Not found Job with id " + id));
//		
//		job.setName(jobRequest.getName());
//		job.setStartDate(jobRequest.getStartDate());
//		job.setEndDate(jobRequest.getEndDate());
////		job.setAssigned(jobRequest.isAssigned());
////		job.setTemp(jobRequest.getTemp());
////		
//		return new ResponseEntity<>(jobRepository.save(job), HttpStatus.OK);
//	}
//	
//	@DeleteMapping("/{id}")
//	public ResponseEntity<HttpStatus> deleteJob(@PathVariable("id") long id) {
//		jobService.deleteJob(id);
//		
//		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//	}
}