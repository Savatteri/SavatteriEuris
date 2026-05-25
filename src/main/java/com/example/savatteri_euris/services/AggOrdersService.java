package com.example.savatteri_euris.services;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.savatteri_euris.exceptions.EventCodeException;
import com.example.savatteri_euris.models.aggs.AggOrders;
import com.example.savatteri_euris.models.events.OrderProduct;
import com.example.savatteri_euris.models.events.Orders;
import com.example.savatteri_euris.models.facts.Customer;
import com.example.savatteri_euris.models.facts.Product;
import com.example.savatteri_euris.models.repos.AggOrdersRepo;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Service
@Getter
@Slf4j
public class AggOrdersService {

	@Autowired
	AggOrdersRepo aggOrdersRepo;

	public void save(AggOrders product) {
		aggOrdersRepo.save(product);
	}

	public List<AggOrders> findAll() {
		return aggOrdersRepo.findAll();
	}

	public AggOrders findOneByEventCodeAndProductId(String eventCode, long productId) {
		return aggOrdersRepo.findOneByEventCodeAndProductId(eventCode, productId);
	}

	public void initByOrders(Orders orders, OrderProduct orderProduct) {

		if (orders != null && orderProduct != null) {
			Customer customer = orders.getCustomer();
			long customerId = customer.getId();
			String name = customer.getName();
			String familyName = customer.getFamilyName();
			String eventCode = orders.getEventCode();
			if (eventCode == null || StringUtils.isBlank(eventCode))
				throw new EventCodeException();

			String status = orders.getStatus();

			AggOrders aggOrders = new AggOrders();
			aggOrders.setLastStatusDate(orders.getInsertDate());
			updateInformationsByAggProduct(customerId, name, familyName, eventCode, status, orderProduct, aggOrders);
		} else {
			throw new NullPointerException("orders or orderProduct null");
		}

	}

	public void updateByOrders(Orders orders, OrderProduct orderProduct, AggOrders aggOrders) {

		Customer customer = orders.getCustomer();
		long customerId = customer.getId();
		String name = customer.getName();
		String familyName = customer.getFamilyName();
		String eventCode = orders.getEventCode();
		String status = orders.getStatus();
		aggOrders.setLastStatusDate(orders.getInsertDate());

		updateInformationsByAggProduct(customerId, name, familyName, eventCode, status, orderProduct, aggOrders);

	}

	private void updateInformationsByAggProduct(long customerId, String name, String familyName, String eventCode,
			String status, OrderProduct orderProduct, AggOrders aggOrders) {

		aggOrders.setCustomerId(customerId);
		if (name != null)
			aggOrders.setCustomerName(name);
		if (familyName != null)
			aggOrders.setCustomerFamilyName(familyName);
		if (eventCode != null)
			aggOrders.setEventCode(eventCode);
		else {
			throw new EventCodeException();
		}
		if (status != null)
			aggOrders.setStatus(status);

		Product product = orderProduct.getProduct();
		if (product != null) {
			aggOrders.setProductId(product.getId());
			aggOrders.setProductCode(product.getCode());
			aggOrders.setProductName(product.getName());
			aggOrders.setQuantity(orderProduct.getQuantity());

			aggOrdersRepo.save(aggOrders);
		} else {
			throw new NullPointerException("product null");
		}

	}

}
