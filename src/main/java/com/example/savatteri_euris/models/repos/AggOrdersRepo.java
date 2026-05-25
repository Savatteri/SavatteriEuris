package com.example.savatteri_euris.models.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.savatteri_euris.models.aggs.AggOrders;

public interface AggOrdersRepo extends JpaRepository<AggOrders, Long> {
	public AggOrders findOneById(Long id);

	public AggOrders findOneByEventCodeAndProductId(String eventCode, long productId);

}
