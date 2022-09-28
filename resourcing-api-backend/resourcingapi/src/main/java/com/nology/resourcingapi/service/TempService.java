package com.nology.resourcingapi.service;

//import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

import com.nology.resourcingapi.dto.TempCreateDTO;
import com.nology.resourcingapi.dto.TempDTO;
import com.nology.resourcingapi.entity.Temp;
import com.nology.resourcingapi.exception.ResourceNotFoundException;
import com.nology.resourcingapi.repository.TempRepository;

@Service
@Transactional
public class TempService {
	@Autowired
	private TempRepository tempRepository;
	
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
}
