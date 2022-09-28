package com.nology.resourcingapi.dto;

import java.util.Date;

import javax.validation.constraints.NotNull;

public class JobCreateDTO {
	@NotNull
	private String name;
	@NotNull
	private Date startDate;
	@NotNull
	private Date endDate;
	
	public String getName() {
		return name;
	}
	public Date getStartDate() {
		return startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	
}
