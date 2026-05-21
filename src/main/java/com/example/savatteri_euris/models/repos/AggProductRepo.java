package com.example.savatteri_euris.models.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.savatteri_euris.models.aggs.AggProduct;
import com.example.savatteri_euris.models.facts.Customer;
import com.example.savatteri_euris.models.facts.Product;


public interface AggProductRepo extends JpaRepository<AggProduct, Long> {
	public AggProduct findOneById(Long id);
	public AggProduct findOneByCode(String code);


}
