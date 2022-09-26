package com.nology.resourcingapi.dto;

import java.util.Date;

import javax.validation.constraints.NotNull;

import com.nology.resourcingapi.entity.Temp;

public class JobDTO {
	@NotNull
	private String name;
	
	@NotNull
	private Date startDate;
	
	@NotNull
	private Date endDate;
	
//	private boolean assigned;
//	
//	// many to one relationship --> many jobs can be assigned to one temp
//	private Temp temp;
	
	
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
	
//	public boolean isAssigned() {
//		return assigned;
//	}
//	public void setAssigned(boolean assigned) {
//		this.assigned = assigned;
//	}
//	
//	public Temp getTemp() {
//		return temp;
//	}
//	public void setTemp(Temp temp) {
//		this.temp = temp;
//	}
//	
	
}
