package com.nology.resourcingapi.entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "temps")
public class Temp {
	
//	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "temp_generator")
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", updatable = false, nullable = false)
	private Long id;
	
	@Column(name = "first_name")
	private String firstName;
	
	@Column (name = "last_name")
	private String lastName;
	
//	@Column(name = "rating")
//	private int rating;
	
//	// one to many relationship -- many jobs can be assigned to one temp
//	@OneToMany(fetch = FetchType.LAZY, mappedBy="temp")
////		@JoinColumn(name = "temp_id", nullable = false)
////	@JoinColumn(name = "job_id")
////		@OnDelete(action = OnDeleteAction.CASCADE) // we don't need the jobs to be deleted at same time as a Temp
//	@JsonIgnore
//	private Set<Job> jobs;
	
	@OneToMany(mappedBy="temp", cascade = CascadeType.ALL)
//	@JoinColumn(name = "fk_temp_id", referencedColumnName = "id")
	private Set<Job> jobs = new HashSet<Job>();
//	private List<Job> jobs;
	
	
	
	public Temp(String firstName, String lastName) {
		this.firstName = firstName;
		this.lastName = lastName;
//		this.rating = rating;
	}
	
	public Temp(String firstName, String lastName, Set<Job> jobs) {
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

	public Set<Job> getJobs() {
		return jobs;
	}

	public void setJobs(Set<Job> jobs) {
		this.jobs = jobs;
		
//		for(Job j : jobs) {
//			j.setTemp(this);
//		}
	}

	
}

