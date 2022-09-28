package com.nology.resourcingapi.dto;

import java.util.Set;

import javax.validation.constraints.NotNull;

import com.nology.resourcingapi.entity.Job;

public class TempReadDTO {
	
	@NotNull
	private String firstName;
	
	@NotNull
	private String lastName;
	
	// one to many relationship
	private Set<Job> jobs;
	
	
	public String getFirstName() {
		return firstName;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public Set<Job> getJobs() {
		return jobs;
	}

}
