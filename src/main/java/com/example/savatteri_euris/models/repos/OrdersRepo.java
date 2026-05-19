package com.example.savatteri_euris.models.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.savatteri_euris.models.events.Orders;


public interface OrdersRepo extends JpaRepository<Orders, Long> {

}
