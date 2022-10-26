package com.nology.job;

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

import com.nology.temp.TempRepository;
import com.nology.exceptions.JobDatesClashException;
import com.nology.exceptions.ResourceNotFoundException;
import com.nology.temp.Temp;

@Service
@Transactional
public class JobService {
	@Autowired
	private JobRepository jobRepository;
	@Autowired
	private TempRepository tempRepository;
//	@Autowired
//	private TempService tempService;
	
	public List<Job> getAllJobs() {
		return jobRepository.findAll();
	}
	
	public Optional<Job> getJobById(@PathVariable long id) {
		return jobRepository.findById(id);
	}
	
	public Job create(@Valid JobCreateDTO jobCreateRequest) throws Exception {
		Job dbJob = null;
		String incomingName = jobCreateRequest.getName();
		Date incomingStartDate = jobCreateRequest.getStartDate();
        Date incomingEndDate = jobCreateRequest.getEndDate();
        Long incomingTempId = jobCreateRequest.getTempId();
        
        
        // Check that the endDate is not prior to the startDate
        checkStartDateIsNotAfterEndDate (incomingStartDate,incomingEndDate);

        // Check if job create request has been submitted with a Temp assigned to it.
        // If so, check if the Temp's currently assigned job dates clash. If no clash, 
        // return Job create request including Temp id.
        // If no Temp assigned, return Job create request (with no Temp id). 
        // Temp can be assigned later via a PUT request. 
        if (incomingTempId != null) {
            System.out.println("YES Temp Id provided");  
            Temp incomingTemp = tempRepository.findById(incomingTempId)
                    .orElseThrow(() -> new ResourceNotFoundException("No Temp with id = " + incomingTempId + " found."));
            List<Job> assignedJobs = jobRepository.findByDateBetween(incomingStartDate, incomingEndDate, incomingTempId);
            for (Job j : assignedJobs) {
                System.out.println(j.getId());                
            }
            if (assignedJobs.size() > 0) {
                throw new JobDatesClashException("Currently assigned job dates clash for the Temp: tempId = " + 
                                                    incomingTemp.getId() + ", " + 
                                                    incomingTemp.getFirstName() + " " + 
                                                    incomingTemp.getLastName());
            } else {
                dbJob = new Job(incomingName, incomingStartDate, incomingEndDate, incomingTemp);  
            }
        } else {
            System.out.println("no temp Id provided");                
            dbJob = new Job(incomingName, incomingStartDate, incomingEndDate);  
        }

		Job savedJob = jobRepository.save(dbJob);
		Optional<Job> retrievedJob = jobRepository.findById(savedJob.getId());
		if (retrievedJob.isPresent()) {
		    return savedJob;
		} else {
		    throw new Exception("Error when trying to save job to database.");
		}
	}
	
	protected void checkStartDateIsNotAfterEndDate(Date starDate, Date endDate) {
	 // Check that the endDate is not prior to the startDate
        if (starDate.compareTo(endDate) > 0) {
            throw new JobDatesClashException("Failed to create job. " +
                          "Start date needs to be same date or prior to the end date.");
        }
	}
	
	public void deleteJob(@PathVariable long id) {
		jobRepository.findById(id).orElseThrow(() -> 
			new ResourceNotFoundException("Job not found with id :" + id));
		
		jobRepository.deleteById(id);;
	}
	
	protected void updateJobDate(Job existingJob, 
                            Date startDateRequest, 
                            Date endDateRequest, 
                            Date existingStartDate, 
                            Date existingEndDate, 
                            Long tempId) {
	    if (startDateRequest !=null && 
                endDateRequest != null &&
                jobRepository.findByDateBetween(startDateRequest, endDateRequest, tempId).size() == 0) {
	        checkStartDateIsNotAfterEndDate(startDateRequest, endDateRequest);
	        existingJob.setStartDate(startDateRequest);
	        existingJob.setEndDate(endDateRequest);
        }
        else if (startDateRequest != null && 
                startDateRequest.compareTo(existingEndDate) <= 0 &&
                jobRepository.findByStartDateBetween(startDateRequest, existingEndDate, tempId).size() == 0) {
            checkStartDateIsNotAfterEndDate(startDateRequest, existingEndDate);
            existingJob.setStartDate(startDateRequest);
        }
        else if (endDateRequest != null &&
                existingStartDate.compareTo(endDateRequest) <= 0 &&
                jobRepository.findByEndDateBetween(existingStartDate, endDateRequest, tempId).size() == 0) {
            checkStartDateIsNotAfterEndDate(existingStartDate, endDateRequest);
            existingJob.setEndDate(endDateRequest);
        }	    
	}
	
	public Job partiallyUpdateJob(@PathVariable Long id, JobUpdateDTO jobUpdateRequest) {
		// check whether the job with given id exists in the DB
		Job existingJob = getJobById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Job with id: " + id + "not found"));
		String nameRequest = jobUpdateRequest.getName();
		Date startDateRequest = jobUpdateRequest.getStartDate();
		Date endDateRequest = jobUpdateRequest.getEndDate();
        Long tempIdRequest = jobUpdateRequest.getTempId();
		Date existingStartDate = existingJob.getStartDate();
		Date existingEndDate = existingJob.getEndDate();
		Temp existingTemp = existingJob.getTemp();
		
		if (nameRequest != null)
		    existingJob.setName(nameRequest);
		
		if (tempIdRequest != null) {
		    // Check if requested Temp exists
		    Temp requestedTemp = tempRepository.findById(tempIdRequest)
                    .orElseThrow(() -> new ResourceNotFoundException("No Temp with id = " + tempIdRequest + " found."));
		    // Check that requested Temp's currently assigned job dates do not clash
		    if (requestedTemp.getJobs().size() > 0) {
		        updateJobDate(existingJob, 
		                startDateRequest, 
		                endDateRequest, 
		                existingStartDate, 
		                existingEndDate, 
		                tempIdRequest);
		    }
            existingJob.setTemp(requestedTemp);
        } 
		// CHECK FOR DATE CLASHES IF THERE IS A TEMP ASSIGNED TO THE JOB
        else if (existingTemp != null && existingTemp.getJobs().size() > 0) {
		    updateJobDate(existingJob, 
					startDateRequest, 
					endDateRequest, 
					existingStartDate, 
					existingEndDate, 
					existingJob.getTemp().getId());
		} 
		if (tempIdRequest == null && existingTemp == null && startDateRequest != null && endDateRequest == null){
            checkStartDateIsNotAfterEndDate(startDateRequest, existingEndDate);
		    existingJob.setStartDate(startDateRequest);
		}
		if (tempIdRequest == null && existingTemp == null && startDateRequest == null && endDateRequest != null){
            checkStartDateIsNotAfterEndDate(existingStartDate, endDateRequest);
            existingJob.setEndDate(endDateRequest);
        }
		
		Job updatedJob = jobRepository.save(existingJob);
        return updatedJob;
	}
	
	public List<Job> searchJobs(String query) {
		List<Job> jobs = jobRepository.searchJobsSQL(query);
		return jobs;
	}
	
	public List<Job> searchJobsAssigned(boolean isAssigned) {
		if (isAssigned)
			return jobRepository.searchJobsAssignedNotNullSQL();
		else 
			return jobRepository.searchJobsAssignedNullSQL();
	}
}

