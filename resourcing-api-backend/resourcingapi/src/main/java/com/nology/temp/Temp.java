package com.nology.temp;


import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import com.nology.job.Job;

@Entity
@Table(name = "temps")
public class Temp {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", updatable = false)
	private Long id;
	
	@Column(name = "first_name")
	private String firstName;
	
	@Column (name = "last_name")
	private String lastName;
	
	@JsonIgnoreProperties("temp")
	@OneToMany(mappedBy="temp", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<Job> jobs;

	
	public Temp(String firstName, String lastName) {
		this.firstName = firstName;
		this.lastName = lastName;
	}
	
	public Temp(String firstName, String lastName, List<Job> jobs) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.jobs = jobs;
	}
	
	public Temp() {
		
	}

	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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

	public List<Job> getJobs() {
		return jobs;
	}

	public void setJobs(List<Job> jobs) {
		this.jobs = jobs;
	}

	
}


