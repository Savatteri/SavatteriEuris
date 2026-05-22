package com.example.savatteri_euris.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.savatteri_euris.exceptions.StockException;
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
import com.example.savatteri_euris.utils.OrderUtil;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Service
@Getter
@Slf4j
public class OrdersService {

	@Autowired
	OrdersRepo ordersRepo;
	@Autowired
	CustomerService customerService;
	@Autowired
	ProductService productService;
	@Autowired
	AggProductService aggProductService;

	public void saveByDto(OrderDto orderDto) {
		
		String customerId = orderDto.getCustomerId();

		Customer customer = getCustomerService().findOneById(Long.valueOf(customerId));

		Orders orders = new Orders();

		orders.setCustomer(customer);
		orders.setInsertDate(new Date());
		orders.setStatus(OrderUtil.STATUS_WAITING);

		List<OrderProduct> orderProducts = new ArrayList<>();
		orderDto.getProducts().forEach(productMap -> {
			productMap.entrySet().forEach(entry -> {

				String productIdStr = entry.getKey();
				Integer quantity = entry.getValue();

				Long productId = Long.valueOf(productIdStr);

				OrderProduct orderProduct = new OrderProduct();
				orderProduct.setQuantity(quantity);
				Product product = getProductService().findOneById(productId);
				orderProduct.setProduct(product);
				orderProduct.setOrders(orders);
				orderProducts.add(orderProduct);
			});
		});
		orders.setOrderProducts(orderProducts);
		ordersRepo.save(orders);
		
	}

	public List<OrderDto> findAll() {
		
		List<Orders> orders = ordersRepo.findAll();
		List<OrderDto> orderDtos = new ArrayList<>();

		orders.forEach(order -> {
			OrderDto orderDto = new OrderDto();
			orderDto.setCustomerId(order.getCustomer().getId().toString());

			List<Map<String, Integer>> productList = new ArrayList<>();

			order.getOrderProducts().forEach(orderProduct -> {
				Map<String, Integer> orderProductMap = new HashMap<>();

				Integer quantity = orderProduct.getQuantity();
				String productId = orderProduct.getProduct().getId().toString();
				orderProductMap.put(productId, quantity);
				productList.add(orderProductMap);

			});
			orderDto.setProducts(productList);
			orderDtos.add(orderDto);

		});
		return orderDtos;

	}

	public boolean checkAvaiability(List<Map<String, Integer>> products) {

		products.forEach(productFromDto -> {
			productFromDto.entrySet().forEach(entry -> {
				String productIdStr = entry.getKey();
				Integer quantity = entry.getValue();

				if (StringUtils.isNotBlank(productIdStr)) {
					Long productId = Long.valueOf(productIdStr);
					Product product = getProductService().findOneById(productId);
					AggProduct aggProduct = getAggProductService().findOneByCode(product.getCode());
					if (quantity > aggProduct.getStock()) {
						String message = String.format("The order cannot be fulfilled. stock=%s, quantity=%s, code=%s",
								aggProduct.getStock(), quantity, aggProduct.getCode());
						log.error("The order cannot be fulfilled. stock={}, quantity={}, code={}",
								aggProduct.getStock(), quantity, aggProduct.getCode());
						throw new StockException(message);
					}
				}
			});
		});
		return true;
	}

}
