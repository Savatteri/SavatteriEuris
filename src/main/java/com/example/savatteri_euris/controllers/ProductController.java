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

import com.example.savatteri_euris.models.facts.Customer;
import com.example.savatteri_euris.models.facts.Product;
import com.example.savatteri_euris.services.CustomerService;
import com.example.savatteri_euris.services.ProductService;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@RestController
@Getter
@Slf4j
@RequestMapping(ProductController.ROOT_PATH)
public class ProductController {
	
	public static final String ROOT_PATH = "/product";
	
	@Autowired
	private ProductService productService;

	@PostMapping("/insert")
	public ResponseEntity<String> insert(
			@RequestBody Product product) {
		
		log.info("insert operation, customer={}", product);
		
		getProductService().save(product);
		
		return ResponseEntity.ok("insert complete");
		
	}
	
	@GetMapping("/findAll")
    public ResponseEntity<List<Product>> findAll() {
        
        List<Product> productList = getProductService().findAll(); 
        
        return ResponseEntity.ok(productList);
    }
	
}
