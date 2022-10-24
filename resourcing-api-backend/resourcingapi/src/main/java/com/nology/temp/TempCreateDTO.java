package com.nology.temp;

import javax.validation.constraints.NotNull;


public class TempCreateDTO {
	
	@NotNull(message = "Temp first name should not be null")
	private String firstName;
	
	@NotNull(message = "Temp last name should not be null")
	private String lastName;
	
	public String getFirstName() {
		return firstName;
	}
	
	public String getLastName() {
		return lastName;
	}
	
}
