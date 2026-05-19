package com.example.savatteri_euris.models.dtos;

import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderDto {
	
	private String customerId;
	private List<Map<String,Integer>> products;
	
}
