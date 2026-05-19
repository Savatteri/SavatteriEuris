package com.example.savatteri_euris.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.savatteri_euris.models.facts.Customer;
import com.example.savatteri_euris.models.facts.Product;
import com.example.savatteri_euris.models.repos.CustomerRepo;
import com.example.savatteri_euris.models.repos.ProductRepo;

import lombok.Getter;

@Service
@Getter
public class ProductService {
	
	@Autowired
	ProductRepo productRepo;
	
	public void save(Product customer) {
		productRepo.save(customer);
	}
	
	public List<Product> findAll(){
		return productRepo.findAll();
	}
	
	public Product findOneById(Long id) {
		return productRepo.findOneById(id);
	}


}
