package com.nology.temp;

import java.util.ArrayList;
import java.util.Date;
//import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Optional;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

import com.nology.job.JobReadDTO;
import com.nology.job.JobRepository;

import exceptions.ResourceNotFoundException;
import com.nology.job.Job;

@Service
@Transactional
public class TempService {
	@Autowired
	private TempRepository tempRepository;
	@Autowired
	private JobRepository jobRepository;
	
//	public static List<Temp> temps = new ArrayList<Temp>();
	
	public List<Temp> getAllTemps() {
		return tempRepository.findAll();
	}
	
	public Optional<Temp> getTemp(@PathVariable long id) {
		return tempRepository.findById(id);
	}
	
	public ResponseEntity<Object> create(@Valid TempCreateDTO temp) {
		Temp dbTemp  = new Temp(temp.getFirstName(), temp.getLastName());
		Temp savedTemp = tempRepository.save(dbTemp);
		if (tempRepository.findById(savedTemp.getId()).isPresent()) {
			return ResponseEntity.accepted().body("Successfully Created Temp and Jobs");
		} else 
			return ResponseEntity.unprocessableEntity().body("Failed to Create specified Temp");
	}
	
	public void deleteTemp(@PathVariable long id) {
		tempRepository.findById(id).orElseThrow(() -> 
			new ResourceNotFoundException("Job not found with id :" + id));
		
		tempRepository.deleteById(id);
	}
	
	public List<Temp> findByJobId(@PathVariable long jobId) {
		System.out.println("HEREEEE");
		// Get job based on the jobId param
		Job job = jobRepository.findById(jobId).orElseThrow(() -> 
			new ResourceNotFoundException("Job not found with id :" + jobId));
		
		System.out.println("Job is : " + job.getName());
		// Get startDate and endDate from job
		Date jobStartDate = job.getStartDate();
		Date jobEndDate = job.getEndDate();	
		
		// Get all Temps as a list
		List<Temp> temps = tempRepository.findAll();
		List<Temp> availableTemps = new ArrayList<Temp>();
		
		// loop through temps 
		for ( ListIterator<Temp> iter = temps.listIterator(); iter.hasNext(); ) {
			Temp temp = iter.next();
			Set<Job> jobs = temp.getJobs();
			// if temp has no job, it can be added to list
			if (jobs.size() > 0 && jobs != null) {
				// if temp has jobs, loop through the jobs and check no date clash. 
				for (Job assignedJob : jobs) {
					Date assignedJobStartDate = assignedJob.getStartDate();
					Date assignedJobEndDate = assignedJob.getEndDate();
					if ((assignedJobStartDate.compareTo(jobStartDate) <  0
							|| assignedJobStartDate.compareTo(jobEndDate) > 0) 
							&& (assignedJobEndDate.compareTo(jobStartDate) <  0
							|| assignedJobEndDate.compareTo(jobEndDate) > 0)) {
						System.out.println(assignedJob.getName() + " is a job that does not clash");
						continue;
					}
					else 
						iter.remove();
				}
			} 
		}
		return temps;
	}
}
