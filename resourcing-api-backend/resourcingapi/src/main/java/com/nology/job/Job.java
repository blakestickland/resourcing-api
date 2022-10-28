package com.nology.job;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import com.nology.temp.Temp;

@Entity
@Table(name = "jobs")
public class Job { 
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", updatable = false)
	private Long id;
	
	@Column(name = "name")
	private String name;
	
	// Format the Date value to make it readable
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
	@Column(name = "start_date")
	private Date startDate;
	
	// Format the Date value to make it readable
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
	@Column(name = "end_date")
	private Date endDate;
	
//	// many to one relationship -- many jobs can be assigned to one temp
	@JsonIgnoreProperties("jobs")
	@ManyToOne (cascade=CascadeType.ALL, fetch = FetchType.EAGER, optional = true)
	@JoinColumn(name="temp_id", nullable = true)
	private Temp temp;
	

	public Job(String name, Date startDate, Date endDate) {
		this.name = name;
		this.startDate = startDate;
		this.endDate = endDate;
	}
	

	public Job(String name, Date startDate, Date endDate, Temp temp) {
		this.name = name;
		this.startDate = startDate;
		this.endDate = endDate;
		this.temp = temp;
	}
	
	public Job() {
	
	}
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Temp getTemp() {
		return temp;
	}

	public void setTemp(Temp temp) {
		this.temp = temp;
	}
	
}
