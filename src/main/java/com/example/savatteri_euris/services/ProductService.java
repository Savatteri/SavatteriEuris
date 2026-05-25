package com.example.savatteri_euris.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.savatteri_euris.models.facts.Product;
import com.example.savatteri_euris.models.repos.ProductRepo;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Service
@Getter
@Slf4j
public class ProductService {

	@Autowired
	ProductRepo productRepo;

	public void save(Product product) {
		productRepo.save(product);
	}

	public List<Product> findAll() {
		return productRepo.findAll();
	}

	public Product findOneById(Long id) {
		return productRepo.findOneById(id);
	}

	public boolean isValid(Product product) {
		if (product != null) {
			Integer baseStock = product.getBaseStock();

			if (baseStock != null && baseStock >= 0) {
				return true;
			} else {
				log.error("product validation error. baseStock={}", baseStock);
			}
		} else
			log.error("product null");
		return false;

	}

	public void deleteAll() {
		productRepo.deleteAll();
	}

}
