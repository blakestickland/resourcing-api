package com.nology.resourcingapi.controller;

import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.nology.resourcingapi.dto.JobDTO;
import com.nology.resourcingapi.dto.TempDTO;
import com.nology.resourcingapi.entity.Temp;
import com.nology.resourcingapi.exception.ResourceNotFoundException;
import com.nology.resourcingapi.repository.JobRepository;
import com.nology.resourcingapi.repository.TempRepository;
import com.nology.resourcingapi.service.TempService;

//@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/temps")
public class TempController {
	
	@Autowired
	private TempService	tempService;
	
	@Autowired
	private TempRepository tempRepository;
	
	private JobRepository jobRepository; 
	
	@Autowired TempController(TempRepository tempRepository, JobRepository jobRepository) {
		this.tempRepository = tempRepository;
		this.jobRepository = jobRepository;
	}
	
	
	// GET "/temps"
	// can also get all based on search for firstName
//	@GetMapping
//	public ResponseEntity<List<Temp>> getAllTemps(@RequestParam(required = false) String firstName) {
//		List<Temp> temps = new ArrayList<Temp>();
//		
//		if (firstName == null) 
//			tempRepository.findAll().forEach(temps::add);
//		else 
//			tempRepository.findByFirstNameContaining(firstName).forEach(temps::add);
//		
//		if (temps.isEmpty()) {
//			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//		}
//		
//		return new ResponseEntity<>(temps, HttpStatus.OK);
//	}
	
	@GetMapping
	public List<Temp> getTemps() {
		return tempService.all();
	}
	
	// GET "/temps/{id}"
	@GetMapping("/{id}")
	public ResponseEntity<Temp> getTempById(@PathVariable("id") long id) {
		Temp temp = tempRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Not found Temp with id = " + id));
		return new ResponseEntity<>(temp, HttpStatus.OK);
	}
	
	// POST "/temps"
	@PostMapping
	@ResponseStatus(value = HttpStatus.CREATED)
	public void saveTemp(@Valid @RequestBody TempDTO temp) {
		tempService.create(temp);
	}

	// DELETE("/temps/{id}")
	@DeleteMapping("/{id}")
	public ResponseEntity<HttpStatus> deleteTemp(@PathVariable("id") long id) {
		tempRepository.deleteById(id);
		
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
//	// List temps that are available for a job based on the jobs date range
//	@GetMapping("?jobId={jobId}")
//	public ResponseEntity<List<Temp>> findByJobId(@PathVariable("jobId") long jobId) {
//		List<Temp> temps = tempRepository.findByJobId(jobId);
//		
//		if (temps.isEmpty()) {
//			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//		}
//		
//		return new ResponseEntity<>(temps, HttpStatus.OK);
//	}
}
