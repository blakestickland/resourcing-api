package com.nology.job;

import java.util.Date;

import javax.validation.constraints.NotNull;

public class JobCreateDTO {
    
	@NotNull(message = "Job name should not be null")
	private String name;
	
	@NotNull(message = "Job start date should not be null")
	private Date startDate;
	
	@NotNull(message = "Job end date should not be null")
	private Date endDate;
	
	private Long tempId;
	
	
	public String getName() {
		return name;
	}
	public Date getStartDate() {
		return startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public Long getTempId() {
		return tempId;
	}
	
}
