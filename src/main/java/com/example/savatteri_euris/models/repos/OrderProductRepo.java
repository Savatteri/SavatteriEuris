package com.example.savatteri_euris.models.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.savatteri_euris.models.events.OrderProduct;


public interface OrderProductRepo extends JpaRepository<OrderProduct, Long> {

}
