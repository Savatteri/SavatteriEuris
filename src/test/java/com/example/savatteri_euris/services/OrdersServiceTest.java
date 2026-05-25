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

import com.example.savatteri_euris.exceptions.EventCodeException;
import com.example.savatteri_euris.exceptions.StockException;
import com.example.savatteri_euris.models.aggs.AggOrders;
import com.example.savatteri_euris.models.aggs.AggProduct;
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
public class OrdersServiceTest {

	@Autowired
	OrdersRepo ordersRepo;
	@Autowired
	OrderProductRepo orderProductRepo;
	@Autowired
	QueueOrdersModifiedRepo queueOrdersModifiedRepo;
	@Autowired
	QueueProductModifiedRepo queueProductModifiedRepo;
	@Autowired
	ProductRepo productRepo;
	@Autowired
	CustomerRepo customerRepo;


	
	@Autowired
    OrdersService ordersService; 

	@Autowired
    CustomerService customerService;
	@Autowired
	ProductService productService;
	@MockBean
	AggProductService aggProductService;
	@MockBean
	AggOrdersService aggOrdersService;

	@BeforeEach
	private void deleteAll() {
		orderProductRepo.deleteAll();
		ordersRepo.deleteAll();
		productRepo.deleteAll();
		customerRepo.deleteAll();
		queueOrdersModifiedRepo.deleteAll();
		queueProductModifiedRepo.deleteAll();
	}

	@Test
    void shouldSaveByDto() {    	
    	Customer customerToDb = new Customer();
    	customerToDb.setName("customerName");
    
    	getCustomerService().save(customerToDb);
    	Customer customerFromDb = getCustomerService().findAll().get(0);
    	//when(getCustomerService().findOneById(any())).thenReturn(customer);

    	Product productToDb = new Product();
    	productToDb.setBaseStock(10);
    	productToDb.setName("product1");
    	productToDb.setCode("code");
    	
    	getProductService().save(productToDb);
    	Product productFromDb = getProductService().findAll().get(0);
    	
    	//when(getProductService().findOneById(any())).thenReturn(productFromDb);
    	
    	OrderDto orderDto = new OrderDto();
    	
    	orderDto.setCustomerId(customerFromDb.getId().toString());
    	List<Map<String, Integer>> products = new ArrayList<>();
    	Map<String, Integer> product = Map.of(
    			productFromDb.getId().toString(), 10
    		);
    	products.add(product);
    	
    	orderDto.setProducts(products);
    	
    	getOrdersService().saveByDto(orderDto);
    	
    	Orders orders = getOrdersService().findOneByCustomerId(customerFromDb.getId());
    	
    	assertEquals(orders.getStatus(),OrderUtil.STATUS_WAITING);
	
    }
	
	@Test
	void shouldGenerateEventCode() {
		List<OrderProduct> orderProducts = new ArrayList<>();
		OrderProduct orderProduct = new OrderProduct();
		Product product = new Product();
		product.setId(1L);
		product.setCode("code");
		product.setName("name");
		orderProduct.setProduct(product);
		
		orderProducts.add(orderProduct);
		Orders orders = new Orders();
		orders.setOrderProducts(orderProducts);
		Customer customer = new Customer();
		customer.setName("customerName");
		
		orders.setCustomer(customer);
		
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, 2026);
		calendar.set(Calendar.MONTH, Calendar.MAY);
		calendar.set(Calendar.DAY_OF_MONTH, 25);
		calendar.set(Calendar.HOUR_OF_DAY, 10);
		calendar.set(Calendar.MINUTE, 30);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);

		Date fixedDate = calendar.getTime();

		orders.setInsertDate(fixedDate);
		
		String eventCodeActual = getOrdersService().generateEventCode(orders);
		
    	assertEquals("260525103000customerName1",eventCodeActual);

	}
	
	@Test
	void shouldNotSetStatusDeleted() {
		AggOrders aggOrders = new AggOrders();
		aggOrders.setStatus(OrderUtil.STATUS_DELIVERED);
		
    	when(getAggOrdersService().findOneByEventCodeAndProductId(any(), anyLong())).thenReturn(aggOrders);
    	
    	boolean result = getOrdersService().setStatusDeleted("eventCode", 1L);
    	
    	assertEquals(false,result);
		
	}
	
	@Test
	void shouldCheckAvaiability() {
		
    	Product productToDb = new Product();
    	productToDb.setBaseStock(10);
    	productToDb.setName("product1");
    	productToDb.setCode("code");
    	
    	getProductService().save(productToDb);
    	Product productFromDb = getProductService().findAll().get(0);
    	Long productId = productFromDb.getId();
		
		List<Map<String, Integer>> products = new ArrayList<>();
		
    	Map<String, Integer> product = Map.of(
    			productId.toString(), 1
    		);
    	products.add(product);
    	
    	AggProduct aggProduct = new AggProduct();
    	aggProduct.setStock(10);
    	
    	when(getAggProductService().findOneByCode(any())).thenReturn(aggProduct);

		
		boolean result = getOrdersService().checkAvaiability(products);
		
    	assertEquals(true,result);

		
	}
	
	@Test
	void shouldNotCheckAvaiabilityWhenQuantityGreaterThanStock() {
		
    	Product productToDb = new Product();
    	productToDb.setBaseStock(1);
    	productToDb.setName("product1");
    	productToDb.setCode("code");
    	
    	getProductService().save(productToDb);
    	Product productFromDb = getProductService().findAll().get(0);
    	Long productId = productFromDb.getId();
		
		List<Map<String, Integer>> products = new ArrayList<>();
		
    	Map<String, Integer> product = Map.of(
    			productId.toString(), 11
    		);
    	products.add(product);
    	
    	AggProduct aggProduct = new AggProduct();
    	aggProduct.setStock(10);
    	
    	when(getAggProductService().findOneByCode(any())).thenReturn(aggProduct);

       	assertThrows(StockException.class, () -> getOrdersService().checkAvaiability(products));
		
	}
	
}
