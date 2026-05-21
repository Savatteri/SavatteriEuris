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

import com.example.savatteri_euris.models.aggs.AggProduct;
import com.example.savatteri_euris.models.facts.Customer;
import com.example.savatteri_euris.models.facts.Product;
import com.example.savatteri_euris.services.AggProductService;
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
	@Autowired
	private AggProductService aggProductService;

	@PostMapping("/insert")
	public ResponseEntity<String> insert(
			@RequestBody Product product) {
		
		log.info("insert operation, product={}", product);
		
		if(!getProductService().isValid(product)) {
			return ResponseEntity
					.badRequest()
					.body("invalid product");
		}
		
		getProductService().save(product);
		
		saveAggProduct(product);
		
		return ResponseEntity.ok("insert complete");
		
	}
	
	@GetMapping("/findAll")
    public ResponseEntity<List<Product>> findAll() {
        
        List<Product> productList = getProductService().findAll(); 
        
        return ResponseEntity.ok(productList);
    }
	
	private void saveAggProduct(Product product) {
		AggProduct aggProduct = new AggProduct();
		aggProduct.setCode(product.getCode());
		aggProduct.setName(product.getName());
		aggProduct.setStock(product.getBaseStock());
		getAggProductService().save(aggProduct);
	}
}
