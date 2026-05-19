package com.example.savatteri_euris.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.savatteri_euris.models.facts.Customer;
import com.example.savatteri_euris.services.CustomerService;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@RestController
@Getter
@Slf4j
@RequestMapping(CustomerController.ROOT_PATH)
public class CustomerController {
	
	public static final String ROOT_PATH = "/customer";
	
	@Autowired
	private CustomerService customerService;

	@PostMapping("/insert")
	public Boolean insert(
			@RequestBody Customer customer) {
		log.info("insert operation, customer={}", customer);
		
		
		
		getCustomerService().save(customer);
		
		return true;
	}
}
