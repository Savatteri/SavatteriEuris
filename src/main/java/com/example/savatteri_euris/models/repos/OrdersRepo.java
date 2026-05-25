package com.example.savatteri_euris.models.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.savatteri_euris.models.events.Orders;


public interface OrdersRepo extends JpaRepository<Orders, Long> {
	public List<Orders> findByEventCode(String eventCode);
	public Orders findFirstByEventCodeAndStatus(String eventCode, String status);
	public Orders findOneByCustomerId(Long id);
	

}
