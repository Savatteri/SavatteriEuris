package com.example.savatteri_euris.models.dtos;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ProductDto {

	private String code;
	private String name;
	private Integer baseStock;
		
}
