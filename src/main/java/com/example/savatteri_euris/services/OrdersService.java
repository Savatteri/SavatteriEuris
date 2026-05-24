package com.example.savatteri_euris.services;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.savatteri_euris.exceptions.StockException;
import com.example.savatteri_euris.models.aggs.AggOrders;
import com.example.savatteri_euris.models.aggs.AggProduct;
import com.example.savatteri_euris.models.dtos.OrderDto;
import com.example.savatteri_euris.models.events.OrderProduct;
import com.example.savatteri_euris.models.events.Orders;
import com.example.savatteri_euris.models.facts.Customer;
import com.example.savatteri_euris.models.facts.Product;
import com.example.savatteri_euris.models.queues.QueueOrdersModified;
import com.example.savatteri_euris.models.queues.QueueProductModified;
import com.example.savatteri_euris.models.repos.CustomerRepo;
import com.example.savatteri_euris.models.repos.OrderProductRepo;
import com.example.savatteri_euris.models.repos.OrdersRepo;
import com.example.savatteri_euris.models.repos.ProductRepo;
import com.example.savatteri_euris.utils.OrderUtil;

import jakarta.transaction.Transactional;
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
	@Autowired
	AggOrdersService aggOrdersService;
	@Autowired
	QueueProductModifiedService queueProductModifiedService;
	@Autowired
	QueueOrdersModifiedService queueOrdersModifiedService;

	public void save(Orders orders) {
		ordersRepo.save(orders);
	}

	public Orders findFirstByEventCodeAndStatus(String eventCode, String status) {
		return ordersRepo.findFirstByEventCodeAndStatus(eventCode, status);
	}

	public List<Orders> findByEventCode(String eventCode) {
		return ordersRepo.findByEventCode(eventCode);
	}

	@Transactional
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
		String eventCode = generateEventCode(orders);
		log.info("eventCode={} generated", eventCode);
		orders.setEventCode(eventCode);
		ordersRepo.save(orders);

		insertEventCodeToProductQueue(eventCode);
		insertEventCodeToOrdersQueue(eventCode);

	}

	private void insertEventCodeToOrdersQueue(String eventCode) {

		QueueOrdersModified queueOrdersModified = new QueueOrdersModified();
		queueOrdersModified.setEventCode(eventCode);
		queueOrdersModified.setInsertDate(new Date());
		queueOrdersModified.setLock(false);

		getQueueOrdersModifiedService().save(queueOrdersModified);
	}

	private void insertEventCodeToProductQueue(String eventCode) {

		QueueProductModified queueProductModified = new QueueProductModified();
		queueProductModified.setEventCode(eventCode);
		queueProductModified.setInsertDate(new Date());
		queueProductModified.setLock(false);

		getQueueProductModifiedService().save(queueProductModified);

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

	private String generateEventCode(Orders orders) {

		StringBuilder sb = new StringBuilder();

		appendDateToSb(orders.getInsertDate(), sb);
		sb.append(orders.getCustomer().getName());

		List<OrderProduct> orderProducts = orders.getOrderProducts();
		orderProducts.forEach(orderProduct -> {

			Product product = orderProduct.getProduct();
			sb.append(product.getId());

		});
		return sb.toString();

	}

	private void appendDateToSb(Date date, StringBuilder sb) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMddHHmmss");
		LocalDateTime localDateTime = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
		sb.append(localDateTime.format(formatter));
	}

	public void setStatusDelivered(String eventCode, long productId) {

		AggOrders aggOrders = getAggOrdersService().findOneByEventCodeAndProductId(eventCode, productId);
		if (aggOrders != null) {
			updateStatus(aggOrders, eventCode, productId, OrderUtil.STATUS_DELIVERED);
		}

	}

	@Transactional
	public boolean setStatusDeleted(String eventCode, long productId) {
		
		AggOrders aggOrders = getAggOrdersService().findOneByEventCodeAndProductId(eventCode, productId);
		
		if (aggOrders != null) {

			if (StringUtils.compareIgnoreCase(aggOrders.getStatus(), OrderUtil.STATUS_DELIVERED) == 0)
				return false;
			else {
				updateStatus(aggOrders, eventCode, productId, OrderUtil.STATUS_DELETED);
				AggProduct aggProduct = getAggProductService().findOneByCode(aggOrders.getProductCode());
				getAggProductService().increaseStock(aggProduct.getId(), aggOrders.getQuantity());
				return true;
			}
		}
		return false;
		
	}

	private void updateStatus(AggOrders aggOrders, String eventCode, long productId, String status) {

		Orders orders = new Orders();
		Customer customer = getCustomerService().findOneById(aggOrders.getCustomerId());

		orders.setCustomer(customer);
		orders.setInsertDate(new Date());
		orders.setStatus(status);

		List<OrderProduct> orderProducts = new ArrayList<>();
		OrderProduct orderProduct = new OrderProduct();
		orderProduct.setQuantity(aggOrders.getQuantity());
		Product product = getProductService().findOneById(productId);
		orderProduct.setProduct(product);
		orderProduct.setOrders(orders);
		orderProducts.add(orderProduct);

		orders.setOrderProducts(orderProducts);
		log.info("eventCode={} generated", eventCode);
		orders.setEventCode(eventCode);
		ordersRepo.save(orders);

		insertEventCodeToOrdersQueue(eventCode);

	}

}
