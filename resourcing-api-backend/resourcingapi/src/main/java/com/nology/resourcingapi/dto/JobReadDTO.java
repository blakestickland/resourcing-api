package com.nology.resourcingapi.dto;

import java.util.Date;

import javax.validation.constraints.NotNull;

import com.nology.resourcingapi.entity.Temp;

public class JobReadDTO {
	@NotNull
	private String name;
	@NotNull
	private Date startDate;
	@NotNull
	private Date endDate;
	private Temp temp;
	
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
