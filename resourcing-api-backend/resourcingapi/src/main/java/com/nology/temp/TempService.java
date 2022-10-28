package com.nology.temp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

import com.nology.job.JobRepository;
import com.nology.exceptions.ResourceNotFoundException;
import com.nology.job.Job;

@Service
@Transactional
public class TempService {
	@Autowired
	private TempRepository tempRepository;
	@Autowired
	private JobRepository jobRepository;
		
	public List<Temp> getAllTemps() {
		return tempRepository.findAll();
	}
	
	public List<Temp> findByFirstName(String firstName) {
        return tempRepository.findByFirstNameContaining(firstName);
    }
	
	public Optional<Temp> getTemp(@PathVariable long id) {
		return tempRepository.findById(id);
	}
	
	public Temp create(@Valid TempCreateDTO tempCreateRequest) {
        Temp dbTemp  = new Temp(tempCreateRequest.getFirstName(), tempCreateRequest.getLastName());
        Temp savedTemp = tempRepository.save(dbTemp);
        Long savedTempId = savedTemp.getId();
        Temp retrievedTemp = tempRepository.findById(savedTempId)
                .orElseThrow(() -> new ResourceNotFoundException("No Temp with id = " + savedTempId + " found."));
        return retrievedTemp;
	}
	
	public void deleteTemp(@PathVariable long id) {
		tempRepository.findById(id).orElseThrow(() -> 
			new ResourceNotFoundException("Job not found with id :" + id));
		
		tempRepository.deleteById(id);
	}
	
	public List<Temp> getTempsByJobId(@PathVariable long jobId) {
	    List<Temp> tempsAvailable = new ArrayList<>();
        List<Temp> temps = tempRepository.findAll();
	    
	 // Get job based on the jobId param
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found with id :" + jobId));
        
        Date jobStartDate = job.getStartDate();
        Date jobEndDate = job.getEndDate();
        
        Temp temp = job.getTemp();
        
        // If there is a temp currently assigned to the job, add them to the available list.
        if (job.getTemp() != null)
            tempsAvailable.add(temp);
        
        // For each temp: 
        // 1. if there are no jobs already assigned to the temp, add them to the available list.
        // 2. if there are jobs assigned, check whether the assigned job dates clash with the incoming job. 
        //    If no clash, add the temp to the available list.
        for (Temp t : temps) {
            if (t.getJobs().isEmpty()) {
                tempsAvailable.add(t);
            }
            else if (!t.getJobs().isEmpty() && jobRepository.findByDateBetween(jobStartDate, jobEndDate, t.getId()).size() == 0) {
                tempsAvailable.add(t);
            }
        }
       
	    return tempsAvailable;
	}
}
