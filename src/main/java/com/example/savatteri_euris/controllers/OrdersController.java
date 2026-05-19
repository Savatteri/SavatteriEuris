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

import com.example.savatteri_euris.models.dtos.OrderDto;
import com.example.savatteri_euris.models.facts.Customer;
import com.example.savatteri_euris.services.CustomerService;
import com.example.savatteri_euris.services.OrdersService;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@RestController
@Getter
@Slf4j
@RequestMapping(OrdersController.ROOT_PATH)
public class OrdersController {
	
	public static final String ROOT_PATH = "/orders";
	
	@Autowired
	private OrdersService ordersService;

	@PostMapping("/insert")
	public ResponseEntity<String> insert(
			@RequestBody OrderDto orderDto) {
		
		log.info("insert operation, order={}", orderDto);
		
		getOrdersService().saveByDto(orderDto);
		
		return ResponseEntity.ok("insert complete");
		
	}
	
	@GetMapping("/findAll")
    public ResponseEntity<List<OrderDto>> findAll() {
        
        List<OrderDto> orderDtos = getOrdersService().findAll(); 
        
        return ResponseEntity.ok(orderDtos);
    }
	
}
