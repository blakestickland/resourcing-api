package com.nology.job;

import java.util.Date;

import javax.validation.constraints.NotNull;

import com.nology.temp.Temp;

public class JobReadDTO {
	@NotNull
	private String name;
	@NotNull
	private Date startDate;
	@NotNull
	private Date endDate;
	private Temp temp;
	
	public JobReadDTO() {}
	
	public JobReadDTO(String name, Date startDate, Date endDate, Temp temp) {
		this.name = name;
		this.startDate = startDate;
		this.endDate = endDate;
		this.temp = temp;
	}
	
	public JobReadDTO(String name, Date startDate, Date endDate) {
		this.name = name;
		this.startDate = startDate;
		this.endDate = endDate;
	}
	
	public String getName() {
		return name;
	}
	public Date getStartDate() {
		return startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public Temp getTemp() {
		return temp;
	}
}
