package com.nology.resourcingapi.service;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

import com.nology.resourcingapi.dto.JobCreateDTO;
import com.nology.resourcingapi.dto.JobDTO;
import com.nology.resourcingapi.dto.JobReadDTO;
import com.nology.resourcingapi.dto.JobUpdateDTO;
import com.nology.resourcingapi.dto.TempCreateDTO;
import com.nology.resourcingapi.entity.Job;
import com.nology.resourcingapi.entity.Temp;
import com.nology.resourcingapi.exception.FieldInvalidError;
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
	@Autowired
	private TempService tempService;
	
	public List<Job> getAllJobs() {
		return jobRepository.findAll();
	}
	
	public Optional<Job> getJob(@PathVariable long id) {
//		Optional<Job> job = jobRepository.findById(id);
//		if (job.get().getTemp() != null) {
//			System.out.println("This Job has a Temp associated with it.");
//			System.out.println(job.get().getTemp());
//			Optional<Temp> assignedTemp = tempService.getTemp(job.get().getTemp().getId());
//			System.out.println(assignedTemp);
////			job.get().setTemp(assignedTemp);
//		}
		return jobRepository.findById(id);
	}
	
	public ResponseEntity<Object> create(@Valid JobCreateDTO jobCreateRequest) {
		Job dbJob;
		// Create a new Job with a Temp associated with it or just create a new Job
		if ((jobCreateRequest.getTempId()) != null) {
			long tempId = jobCreateRequest.getTempId();
			Temp temp = tempRepository.findById(tempId).orElseThrow(() -> 
				new ResourceNotFoundException("Temp not found with id :" + tempId));
			dbJob = new Job(jobCreateRequest.getName(), jobCreateRequest.getStartDate(), jobCreateRequest.getEndDate(), temp);
		} else {
			dbJob = new Job(jobCreateRequest.getName(), jobCreateRequest.getStartDate(), jobCreateRequest.getEndDate());			
		}
		Job savedJob = jobRepository.save(dbJob);
		if (jobRepository.findById(savedJob.getId()).isPresent()) {
			return ResponseEntity.accepted().body("Successfully Created Job");
		} else 
			return ResponseEntity.unprocessableEntity().body("Failed to Create specified Job");
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
			
			// Iterate over the list of jobs assigned to Temp to check if dates clash
			if ((temp.getJobs()) != null && (temp.getJobs()).size() > 0) {
				Date exJStartDate = exisitingJob.getStartDate();
				Date exJEndDate = exisitingJob.getEndDate();
				Set<Job> tempJobsList = temp.getJobs();
				Iterator<Job> itr = tempJobsList.iterator();
				Job tempJob;
				Date tJStartDate;
				Date tJEndDate;
				while (itr.hasNext()) {
					tempJob = itr.next();
					tJStartDate = tempJob.getStartDate();
					tJEndDate = tempJob.getEndDate();
					
					if ((exJStartDate.compareTo(tJEndDate) > 0) &&
						(exJStartDate.compareTo(tJEndDate) < 0) &&
						(exJEndDate.compareTo(tJEndDate) > 0) &&
						(exJEndDate.compareTo(tJStartDate) < 0)) {
						exisitingJob.setTemp(temp);
					} else {
						System.out.println("Check the requested start and end dates for clashes with currently assigned jobs.");
					}
				}
			}
				
			
		}
//				job.setAssigned(jobRequest.isAssigned());
		Job updatedJob = jobRepository.save(exisitingJob);
		
		return updatedJob;
	}
	
	public List<Job> searchJobs(String query) {
		List<Job> jobs = jobRepository.searchJobsSQL(query);
		return jobs;
	}

}

