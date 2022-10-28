package com.nology.job;

import java.util.Date;

import javax.validation.constraints.NotNull;

import com.nology.temp.Temp;

public class JobDTO {
	@NotNull
	private String name;
	
	@NotNull
	private Date startDate;
	
	@NotNull
	private Date endDate;
		
	// many to one relationship --> many jobs can be assigned to one temp
	private Temp temp;
	
	
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
