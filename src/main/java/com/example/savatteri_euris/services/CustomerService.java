package com.example.savatteri_euris.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.savatteri_euris.models.facts.Customer;
import com.example.savatteri_euris.models.repos.CustomerRepo;

@Service
public class CustomerService {
	
	@Autowired
	CustomerRepo customerRepo;
	
	public void save(Customer customer) {
		customerRepo.save(customer);
	}

}
