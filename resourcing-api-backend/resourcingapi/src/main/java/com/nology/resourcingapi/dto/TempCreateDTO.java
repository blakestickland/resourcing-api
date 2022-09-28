package com.nology.resourcingapi.dto;

import javax.validation.constraints.NotNull;


public class TempCreateDTO {
	
	@NotNull
	private String firstName;
	
	@NotNull
	private String lastName;
	
	public String getFirstName() {
		return firstName;
	}
	
	public String getLastName() {
		return lastName;
	}
	
}
