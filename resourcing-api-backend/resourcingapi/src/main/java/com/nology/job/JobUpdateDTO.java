
package com.nology.job;

import java.util.Date;

public class JobUpdateDTO {
	private String name;
	private Date startDate;
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

