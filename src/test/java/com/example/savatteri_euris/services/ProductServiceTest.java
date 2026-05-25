package com.example.savatteri_euris.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;

import com.example.savatteri_euris.models.aggs.AggOrders;
import com.example.savatteri_euris.models.dtos.OrderDto;
import com.example.savatteri_euris.models.events.OrderProduct;
import com.example.savatteri_euris.models.events.Orders;
import com.example.savatteri_euris.models.facts.Customer;
import com.example.savatteri_euris.models.facts.Product;
import com.example.savatteri_euris.models.repos.CustomerRepo;
import com.example.savatteri_euris.models.repos.OrderProductRepo;
import com.example.savatteri_euris.models.repos.OrdersRepo;
import com.example.savatteri_euris.models.repos.ProductRepo;
import com.example.savatteri_euris.models.repos.QueueOrdersModifiedRepo;
import com.example.savatteri_euris.models.repos.QueueProductModifiedRepo;
import com.example.savatteri_euris.utils.OrderUtil;

import lombok.Getter;

@ActiveProfiles("test")
@Getter
@SpringBootTest
public class ProductServiceTest {

	@Autowired
	ProductService productService;

	@BeforeEach
	private void deleteAll() {
	}

	@Test
	void shouldValidateProduct() {
		
		Product product = new Product();
		product.setBaseStock(10);
		
		boolean result = getProductService().isValid(product);
				
    	assertEquals(true,result);

	}
	
	@Test
	void shouldNoValidateProductWhenProductIsNull() {
		
		Product product = null;
		
		boolean result = getProductService().isValid(product);
				
    	assertEquals(false,result);

	}
	
	@Test
	void shouldNoValidateProductWhenBaseStockIsInvalid() {
		
		Product product = new Product();
		product.setBaseStock(-1);
		
		boolean result = getProductService().isValid(product);
				
    	assertEquals(false,result);

	}
	
}
