package com.example.savatteri_euris.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.savatteri_euris.models.aggs.AggProduct;
import com.example.savatteri_euris.models.facts.Customer;
import com.example.savatteri_euris.models.facts.Product;
import com.example.savatteri_euris.models.repos.AggProductRepo;
import com.example.savatteri_euris.models.repos.CustomerRepo;
import com.example.savatteri_euris.models.repos.ProductRepo;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Service
@Getter
@Slf4j
public class AggProductService {

	@Autowired
	AggProductRepo aggProductRepo;

	public void save(AggProduct product) {
		aggProductRepo.save(product);
	}

	public List<AggProduct> findAll() {
		return aggProductRepo.findAll();
	}

	public AggProduct findOneByCode(String code) {
		return aggProductRepo.findOneByCode(code);
	}
	
	public boolean decreaseStock(Long id, int quantity) {
		return aggProductRepo.decreaseStock(id, quantity) == 1 ? true : false;
	}
	
	public boolean increaseStock(Long id, int quantity) {
		return aggProductRepo.increaseStock(id, quantity) == 1 ? true : false;
	}

}
