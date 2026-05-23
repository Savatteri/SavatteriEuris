package com.example.savatteri_euris.models.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.savatteri_euris.models.aggs.AggOrders;
import com.example.savatteri_euris.models.aggs.AggProduct;
import com.example.savatteri_euris.models.facts.Customer;
import com.example.savatteri_euris.models.facts.Product;


public interface AggOrdersRepo extends JpaRepository<AggOrders, Long> {
	public AggOrders findOneById(Long id);
	public AggOrders findOneByEventCodeAndProductId(String eventCode, long productId);


}
