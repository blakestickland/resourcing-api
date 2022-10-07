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
import com.nology.resourcingapi.dto.JobUpdateDTO;
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
	@Autowired
//	private TempService tempService;
	
	public List<Job> getAllJobs() {
		return jobRepository.findAll();
	}
	
	public Optional<Job> getJob(@PathVariable long id) {
		return jobRepository.findById(id);
	}
	
	public ResponseEntity<Object> create(@Valid JobCreateDTO jobCreateRequest) {
		Job dbJob;
		// Check that the endDate is not prior to the startDate
		if (jobCreateRequest.getStartDate().compareTo(jobCreateRequest.getEndDate()) > 0) {
			return ResponseEntity.unprocessableEntity().body(
					"Failed to Create specified Job\nStart date needs to be same date or prior to the end date.");
		}
		// Create a new Job with a Temp associated with it or just create a new Job
		if (jobCreateRequest.getTempId() != null) {
			long tempId = jobCreateRequest.getTempId();
			Temp temp = tempRepository.findById(tempId).orElseThrow(() -> 
				new ResourceNotFoundException("Temp not found with id :" + tempId));
			
			Set<Job> tempJobs = temp.getJobs();
			boolean tempAvailable = true;
			
			// Iterate over the list of jobs assigned to Temp to check if dates clash
			if (tempJobs != null && tempJobs.size() > 0) {
				Date newJobStartDate = jobCreateRequest.getStartDate();
				Date newJobEndDate = jobCreateRequest.getEndDate();
				
				Iterator<Job> itr = tempJobs.iterator();
				
				while (itr.hasNext()) {
					Job itrJob = itr.next();
					Date itrJStartDate = itrJob.getStartDate();
					Date itrJEndDate = itrJob.getEndDate();
					
					if (((newJobStartDate.compareTo(itrJStartDate) >= 0) &&
						(newJobStartDate.compareTo(itrJEndDate) <= 0)) ||
						((newJobEndDate.compareTo(itrJStartDate) >= 0) &&
						(newJobEndDate.compareTo(itrJEndDate) <= 0))) {
						System.out.println("Cannot assign Temp to job.\nClashes with currently assigned job: id=" + itrJob.getId() + " No Temp assigned to job!");
						tempAvailable = false;
						break;
					}
				}
			}
			if (tempAvailable) {
				dbJob = new Job(jobCreateRequest.getName(), jobCreateRequest.getStartDate(), jobCreateRequest.getEndDate(), temp);				
			} else {
				return ResponseEntity.unprocessableEntity().body("Failed to Create specified Job as Temp is currently assigned to a job with dates that clash.");
			}
			
		} else {
			dbJob = new Job(jobCreateRequest.getName(), jobCreateRequest.getStartDate(), jobCreateRequest.getEndDate());			
		}
		Job savedJob = jobRepository.save(dbJob);
		if (jobRepository.findById(savedJob.getId()).isPresent()) {
			return ResponseEntity.accepted().body("Successfully Created Job with id: " + savedJob.getId());
		} else 
			return ResponseEntity.unprocessableEntity().body("Failed to Create specified Job");
	}
	
	public void deleteJob(@PathVariable long id) {
		jobRepository.findById(id).orElseThrow(() -> 
			new ResourceNotFoundException("Job not found with id :" + id));
		
		jobRepository.deleteById(id);;
	}
	
	public ResponseEntity<Object> partiallyUpdateJob(@PathVariable long id, JobUpdateDTO jobUpdateRequest) {
		// check whether the job with given id exists in the DB
		Job exisitingJob = getJob(id)
				.orElseThrow(() -> new ResourceNotFoundException("Job with id: " + id + "not found"));
		String nameRequest = jobUpdateRequest.getName();
		Date startDateRequest = jobUpdateRequest.getStartDate();
		Date endDateRequest = jobUpdateRequest.getEndDate();
		Date exJStartDate = exisitingJob.getStartDate();
		Date exJEndDate = exisitingJob.getEndDate();
		
		if (nameRequest != null)
			exisitingJob.setName(nameRequest);
		
		// CHECK FOR DATE CLASHES IF THERE IS A TEMP ASSIGNED TO THE JOB
		if (startDateRequest != null || endDateRequest != null && exisitingJob.getTemp().getJobs().size() > 0) {
			// loop through Temp's jobs for date clashes ???
			Set<Job> exTempJobs = exisitingJob.getTemp().getJobs();
			
			// Iterate over the list of jobs assigned to Temp to check if dates clash
			Iterator<Job> itr = exTempJobs.iterator();
			
			while (itr.hasNext()) {
				Job tempJob = itr.next();
				Date tJStartDate = tempJob.getStartDate();
				Date tJEndDate = tempJob.getEndDate();
				
				if (tempJob.getId() != id) {
					
					if (startDateRequest != null && endDateRequest != null) {
						if ((startDateRequest.compareTo(tJStartDate) < 0 &&
								endDateRequest.compareTo(tJStartDate) < 0 ||
								startDateRequest.compareTo(tJEndDate) > 0 &&
								endDateRequest.compareTo(tJEndDate) > 0) && 
								startDateRequest.compareTo(endDateRequest) <=0) {
							
								exisitingJob.setStartDate(startDateRequest);
								exisitingJob.setEndDate(endDateRequest);
								
						}
						else {
							return ResponseEntity.unprocessableEntity().body("Failed to update job due to date clash with assigned Temp");						
						}
					} 
					
					else if (startDateRequest != null && endDateRequest == null) {
						if ((startDateRequest.compareTo(tJStartDate) < 0 &&
								exJEndDate.compareTo(tJStartDate) < 0 ||
								startDateRequest.compareTo(tJEndDate) > 0 &&
								exJEndDate.compareTo(tJEndDate) > 0) && 
								startDateRequest.compareTo(exJEndDate) <= 0) {
							
								exisitingJob.setStartDate(startDateRequest);
								
						}
						else {
							return ResponseEntity.unprocessableEntity().body("Failed to update job due to start date clash with assigned Temp");						
						}
					}
					else if (endDateRequest != null && startDateRequest == null) {
						if ((endDateRequest.compareTo(tJStartDate) < 0 && 
								exJStartDate.compareTo(tJStartDate) < 0 ||
								endDateRequest.compareTo(tJEndDate) > 0 &&
								exJStartDate.compareTo(tJEndDate) > 0) && 
								endDateRequest.compareTo(exJStartDate) >= 0) {
							
							System.out.println(exJStartDate);
							System.out.println(endDateRequest);
							System.out.println(startDateRequest);
							
								exisitingJob.setEndDate(endDateRequest);
								
						} else {
							return ResponseEntity.unprocessableEntity().body("Failed to update job due to end date clash with assigned Temp");						
						}
					} 	
				}
				
			}
		}
		
//		if (startDateRequest != null) {
//			if (startDateRequest.compareTo(exJEndDate) <= 0 ||
//					(endDateRequest != null && startDateRequest.compareTo(endDateRequest) <= 0)) {
//				exisitingJob.setStartDate(startDateRequest);
//				exJStartDate = exisitingJob.getStartDate();			
//			} else {
//				return ResponseEntity.unprocessableEntity().body("Failed to update job due to start date clash.");			 
//			}
//		}
//		
//		if (endDateRequest != null) {
//			if (endDateRequest.compareTo(exJStartDate) >= 0) {
//				exisitingJob.setEndDate(endDateRequest);
//				exJEndDate = exisitingJob.getEndDate();				
//			} else {
//				return ResponseEntity.unprocessableEntity().body("Failed to update job due to end date clash.");
//			}
//		} 
		
		
		if (jobUpdateRequest.getTempId() != null) {
			long newTempId = jobUpdateRequest.getTempId();
			Temp newTemp = tempRepository.findById(newTempId).orElseThrow(() -> 
				new ResourceNotFoundException("Temp not found with id :" + id));
			
			Set<Job> newTempJobs = newTemp.getJobs();
			
			// Iterate over the list of jobs assigned to Temp to check if dates clash
			if (newTempJobs != null && newTempJobs.size() > 0) {
				Set<Job> tempJobsList = newTemp.getJobs();
				Iterator<Job> itr = tempJobsList.iterator();
				
				while (itr.hasNext()) {
					Job tempJob = itr.next();
					Date tJStartDate = tempJob.getStartDate();
					Date tJEndDate = tempJob.getEndDate();
					
					if (((exJStartDate.compareTo(tJStartDate) < 0) ||
						(exJStartDate.compareTo(tJEndDate) > 0)) &&
						((exJEndDate.compareTo(tJStartDate) < 0) ||
						(exJEndDate.compareTo(tJEndDate) > 0))) {
						exisitingJob.setTemp(newTemp);
					} else {
						return ResponseEntity.unprocessableEntity().body("Failed to Create specified Job due to Temp currently assigned to a job with dates that clash.");
					}
				}
			}
		}
		Job updatedJob = jobRepository.save(exisitingJob);
		
		return ResponseEntity.ok().body("Successfully Updated Job with id: " + updatedJob.getId());
	}
	
	public List<Job> searchJobs(String query) {
		List<Job> jobs = jobRepository.searchJobsSQL(query);
		return jobs;
	}
	
	public List<Job> searchJobsAssigned(boolean isAssigned) {
		System.out.println(isAssigned);
		List<Job> jobs = null;
		if (isAssigned) {
			jobs = jobRepository.searchJobsAssignedNotNullSQL();
			return jobs;
		}
		else if (!isAssigned) {
			jobs = jobRepository.searchJobsAssignedNullSQL();
			return jobs;
		}
		return jobs;
	}

}

