package com.nology.job;

import java.util.ArrayList;
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

import com.nology.exceptions.ResourceNotFoundException;



@RestController
@RequestMapping(value = "/jobs")
public class JobsController {
	
	@Autowired 
	private JobService jobService;
	
	@GetMapping 
	public ResponseEntity<List<Job>> getAllJobs(@RequestParam(name = "assigned", required = false)  Boolean query) {
	    List<Job> jobs = new ArrayList<Job>();
	    
	    if (query == null)
	        jobService.getAllJobs().forEach(jobs::add);
	    else
	        jobService.searchJobsAssigned(query).forEach(jobs::add);
	    
	    if (jobs.isEmpty()) {
	        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	    }	
	    
        return new ResponseEntity<>(jobs, HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Job> getJobById(@PathVariable(value = "id") Long id) {
	    Job jobData = jobService.getJobById(id)
	            .orElseThrow(() -> new ResourceNotFoundException("No Job with id = " + id + " found."));
	    	    
        return new ResponseEntity<>(jobData, HttpStatus.OK);
	}
	
	@PostMapping
	public ResponseEntity<Job> createJob(@RequestBody @Valid JobCreateDTO job) throws Exception {
		Job newJob = jobService.create(job);
		
		return new ResponseEntity<>(newJob, HttpStatus.CREATED);
	}
	
	
	@PatchMapping("/{id}")
	public ResponseEntity<Job> updateJob(@PathVariable("id") long id,  @RequestBody @Valid JobUpdateDTO jobUpdateRequest) {
		// save mergedJob to jobRepo
	    jobService.getJobById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No Job with id = " + id + " found."));
        
        return new ResponseEntity<>(jobService.partiallyUpdateJob(id, jobUpdateRequest), HttpStatus.OK);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<HttpStatus> deleteJob(@PathVariable("id") long id) {
        jobService.deleteJob(id);
        
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

}
