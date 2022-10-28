package com.nology.temp;

import java.util.Set;

import javax.validation.constraints.NotNull;

import com.nology.job.Job;

public class TempDTO {
	
	@NotNull
	private String firstName;
	
	@NotNull
	private String lastName;
	
	// one to many relationship
	private Set<Job> jobs;
	
	
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public Set<Job> getJobs() {
		return jobs;
	}
	public void setJobs(Set<Job> jobs) {
		this.jobs = jobs;
	}
	

}
