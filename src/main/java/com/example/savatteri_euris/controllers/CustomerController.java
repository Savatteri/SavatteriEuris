package com.example.savatteri_euris.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.savatteri_euris.models.dtos.CustomerDto;
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
	public ResponseEntity<String> insert(
			@RequestBody CustomerDto customerDto) {
		
		log.info("insert operation, customer={}", customerDto);
		
		Customer customer = fromDtoToCustomer(customerDto);
		
		getCustomerService().save(customer);
		
		return ResponseEntity.ok("insert complete");
		
	}
	
	@GetMapping("/findAll")
    public ResponseEntity<List<Customer>> findAll() {
        
        List<Customer> customerList = getCustomerService().findAll(); 
        
        return ResponseEntity.ok(customerList);
    }
	
	private Customer fromDtoToCustomer(CustomerDto customerDto) {
		Customer customer = new Customer();
		customer.setBirthDate(customerDto.getBirthDate());
		customer.setCodiceFiscale(customer.getCodiceFiscale());
		customer.setEmail(customer.getEmail());
		customer.setFamilyName(customer.getFamilyName());
		customer.setName(customer.getName());
		return customer;
	}
	
}
