package com.nology.temp;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import com.nology.exceptions.ResourceNotFoundException;

//@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/temps")
public class TempController {
	
	@Autowired
	private TempService	tempService;

	// GET "/temps"
	// QUERY using @RequestParam lists temps that are available for a job based on the jobs date range
	@GetMapping
	public ResponseEntity<List<Temp>> getAllTemps(@RequestParam(name = "jobid", required = false) Long jobId, @RequestParam(name = "firstname", required = false) String firstName) {
		List<Temp> temps = new ArrayList<Temp>();
        
        if (jobId == null && firstName == null)
            tempService.getAllTemps().forEach(temps::add);
        else if (jobId != null)
            temps = tempService.getTempsByJobId(jobId);
//            tempService.findByJobId(jobId).forEach(temps::add);
        else if (firstName != null)
            tempService.findByFirstName(firstName).forEach(temps::add);
        
        if (temps.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }   
        
        return new ResponseEntity<>(temps, HttpStatus.OK);
    }
	
	// GET "/temps/{id}"
	@GetMapping("/{id}")
	public ResponseEntity<Temp> getTempById(@PathVariable("id") long id) {
		Temp temp = tempService.getTemp(id)
				.orElseThrow(() -> new ResourceNotFoundException("Temp with id = " + id + " not found."));
	      System.out.println(temp.getJobs());

		return new ResponseEntity<>(temp, HttpStatus.OK);
	}
	
	// POST "/temps"
	@PostMapping
	public ResponseEntity<Temp> saveTemp(@Valid @RequestBody TempCreateDTO temp) {
	    Temp newTemp = tempService.create(temp);
	    
		return new ResponseEntity<>(newTemp, HttpStatus.CREATED);
	}

	// DELETE("/temps/{id}")
	@DeleteMapping("/{id}")
	public ResponseEntity<HttpStatus> deleteTemp(@PathVariable("id") long id) {
		tempService.deleteTemp(id);
		
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
