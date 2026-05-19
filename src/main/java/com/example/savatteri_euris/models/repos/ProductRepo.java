package com.example.savatteri_euris.models.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.savatteri_euris.models.facts.Customer;
import com.example.savatteri_euris.models.facts.Product;


public interface ProductRepo extends JpaRepository<Product, Long> {
	public Product findOneById(Long id);

}
