package com.example.savatteri_euris.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.savatteri_euris.exceptions.StockException;
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
	public ResponseEntity<String> insert(@RequestBody OrderDto orderDto) {

		log.info("insert operation, order={}", orderDto);
		try {
			if (getOrdersService().checkAvaiability(orderDto.getProducts())) {

				getOrdersService().saveByDto(orderDto);

			} else {
				log.error("order not avaiable");
				return ResponseEntity.badRequest().body(
						"The order cannot be fulfilled because the requested quantity exceeds the available stock.");
			}
		} catch (StockException e) {
			return ResponseEntity.badRequest().body(e.getMessage());

		}

		return ResponseEntity.ok("insert complete");

	}

	@GetMapping("/findAll")
	public ResponseEntity<List<OrderDto>> findAll() {

		List<OrderDto> orderDtos = getOrdersService().findAll();

		return ResponseEntity.ok(orderDtos);

	}

	@PostMapping("/delivered")
	ResponseEntity<String> statusDelivered(@RequestParam(name = "eventCode", required = true) String eventCode,
			@RequestParam(name = "productId", required = true) long productId) {

		getOrdersService().setStatusDelivered(eventCode, productId);

		return ResponseEntity.ok("update complete");

	}

	@PostMapping("/deleted")
	ResponseEntity<String> statusDeleted(@RequestParam(name = "eventCode", required = true) String eventCode,
			@RequestParam(name = "productId", required = true) long productId) {

		if (getOrdersService().setStatusDeleted(eventCode, productId)) {
			log.info("eventCode={}, productId={} status deleted setted", eventCode, productId);
			return ResponseEntity.ok("update complete");
		} else {
			log.error("eventCode={}, productId={} impossible to update status to deleted", eventCode, productId);
			return ResponseEntity.badRequest().body("unable to update status deleted for products delivered ");
		}

	}
}
