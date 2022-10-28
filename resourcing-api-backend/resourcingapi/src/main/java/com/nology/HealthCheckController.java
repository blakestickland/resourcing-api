package com.nology;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// Health check asks a service on a particular server whether or not is can perform work successfully.

@RestController
@RequestMapping(value = "/")
public class HealthCheckController {
	
	@GetMapping
	public String test() {
    	return "Hello World!!";
    }

}
