package com.example.savatteri_euris.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Date;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.example.savatteri_euris.exceptions.EventCodeException;
import com.example.savatteri_euris.models.aggs.AggOrders;
import com.example.savatteri_euris.models.events.OrderProduct;
import com.example.savatteri_euris.models.events.Orders;
import com.example.savatteri_euris.models.facts.Customer;
import com.example.savatteri_euris.models.facts.Product;
import com.example.savatteri_euris.models.repos.AggOrdersRepo;
import com.example.savatteri_euris.utils.OrderUtil;

import lombok.Getter;

@ActiveProfiles("test")
@Getter
@SpringBootTest
public class AggOrdersServiceTest {
	
	private static final String CUSTOMER_NAME = "customerName";
	private static final String CUSTOMER_FAMILY_NAME = "customerFamilyName";
	private static final String PRODUCT_NAME = "product";
	private static final String PRODUCT_CODE = "productCode";
	private static final String EVENT_CODE = "eventCode";
	private static final int QUANTITY_10 = 10;
	
	@Autowired
	AggOrdersService aggOrdersService;
	@Autowired
	AggOrdersRepo aggOrdersRepo;

    @BeforeEach
    private void deleteAll() {
    	aggOrdersRepo.deleteAll();
    }
    
    @Test
    void shouldInitByOrders() {
    	
    	String eventCode = EVENT_CODE;
    	long productId = 1;
    	
    	Orders orders = new Orders();
    	
    	Customer customer = new Customer();
    	
    	customer.setId(1L);
    	customer.setName(CUSTOMER_NAME);
    	customer.setFamilyName(CUSTOMER_FAMILY_NAME);
    	orders.setCustomer(customer);
    	orders.setEventCode(EVENT_CODE);
    	orders.setInsertDate(new Date());
    	orders.setStatus(OrderUtil.STATUS_WAITING);
    	
    	Product product = new Product();
    	product.setId(productId);
    	product.setCode(PRODUCT_CODE);
    	product.setBaseStock(10);
    	product.setName(PRODUCT_NAME);
    	OrderProduct orderProduct = new OrderProduct();
    	orderProduct.setId(1L);
    	orderProduct.setOrders(orders);
    	orderProduct.setQuantity(QUANTITY_10);
    	orderProduct.setProduct(product);

    	getAggOrdersService().initByOrders(orders, orderProduct);
    	
    	AggOrders aggOrders = aggOrdersRepo.findOneByEventCodeAndProductId(eventCode, productId);
    	
    	assertEquals(EVENT_CODE,aggOrders.getEventCode());
    	assertEquals(productId,aggOrders.getProductId());
    	assertEquals(CUSTOMER_NAME,aggOrders.getCustomerName());
    	assertEquals(CUSTOMER_FAMILY_NAME,aggOrders.getCustomerFamilyName());
    	assertEquals(1L,aggOrders.getCustomerId());
    	assertEquals(PRODUCT_CODE,aggOrders.getProductCode());
    	assertEquals(PRODUCT_NAME,aggOrders.getProductName());
    	assertEquals(QUANTITY_10,aggOrders.getQuantity());
    	assertEquals(OrderUtil.STATUS_WAITING,aggOrders.getStatus());
    	
    }
    
    @Test
    void shouldNotInitByOrdersWhenOrdersAndOrderProductAreNull() {
    	
    	Orders orders = null;
    	OrderProduct orderProduct = null;
       	assertThrows(NullPointerException.class, () -> getAggOrdersService().initByOrders(orders, orderProduct));
       	
    }
    
    @Test
    void shouldNotInitByOrdersWhenEventCodeIsNull() {
    	
    	long productId = 1;
    	
    	Orders orders = new Orders();
    	
    	Customer customer = new Customer();
    	
    	customer.setId(1L);
    	customer.setName(CUSTOMER_NAME);
    	customer.setFamilyName(CUSTOMER_FAMILY_NAME);
    	orders.setCustomer(customer);
    	orders.setEventCode(null);
    	orders.setInsertDate(new Date());
    	orders.setStatus(OrderUtil.STATUS_WAITING);
    	
    	Product product = new Product();
    	product.setId(productId);
    	product.setCode(PRODUCT_CODE);
    	product.setBaseStock(10);
    	product.setName(PRODUCT_NAME);
    	OrderProduct orderProduct = new OrderProduct();
    	orderProduct.setId(1L);
    	orderProduct.setOrders(orders);
    	orderProduct.setQuantity(QUANTITY_10);
    	orderProduct.setProduct(product);
    	
       	assertThrows(EventCodeException.class, () -> getAggOrdersService().initByOrders(orders, orderProduct));
       	
    }
    
    @Test
    void shoudUpdateByOrders() {
    	
    	long productId = 1;
    	
    	Orders orders = new Orders();
    	
    	Customer customer = new Customer();
    	
    	customer.setId(1L);
    	customer.setName(CUSTOMER_NAME);
    	customer.setFamilyName(CUSTOMER_FAMILY_NAME);
    	orders.setCustomer(customer);
    	orders.setEventCode(EVENT_CODE);
    	orders.setInsertDate(new Date());
    	orders.setStatus(OrderUtil.STATUS_WAITING);
    	
    	Product product = new Product();
    	product.setId(productId);
    	product.setCode(PRODUCT_CODE);
    	product.setBaseStock(10);
    	product.setName(PRODUCT_NAME);
    	OrderProduct orderProduct = new OrderProduct();
    	orderProduct.setId(1L);
    	orderProduct.setOrders(orders);
    	orderProduct.setQuantity(QUANTITY_10);
    	orderProduct.setProduct(product);
    	
    	AggOrders aggOrdersNew = new AggOrders();
    	aggOrdersNew.setEventCode(EVENT_CODE);
    	aggOrdersNew.setProductId(1L);
    	aggOrdersRepo.save(aggOrdersNew);
    
    	
    	AggOrders aggOrdersFromDb = aggOrdersRepo.findOneByEventCodeAndProductId(EVENT_CODE, 1L);
    	
    	getAggOrdersService().updateByOrders(orders, orderProduct, aggOrdersFromDb);
    	
    	AggOrders aggOrders = aggOrdersRepo.findOneByEventCodeAndProductId(EVENT_CODE, productId);
    	
    	assertEquals(EVENT_CODE,aggOrders.getEventCode());
    	assertEquals(productId,aggOrders.getProductId());
    	assertEquals(CUSTOMER_NAME,aggOrders.getCustomerName());
    	assertEquals(CUSTOMER_FAMILY_NAME,aggOrders.getCustomerFamilyName());
    	assertEquals(1L,aggOrders.getCustomerId());
    	assertEquals(PRODUCT_CODE,aggOrders.getProductCode());
    	assertEquals(PRODUCT_NAME,aggOrders.getProductName());
    	assertEquals(QUANTITY_10,aggOrders.getQuantity());
    	assertEquals(OrderUtil.STATUS_WAITING,aggOrders.getStatus());
    	
    }
    
}
