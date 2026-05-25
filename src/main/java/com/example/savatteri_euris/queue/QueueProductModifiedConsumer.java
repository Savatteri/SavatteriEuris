package com.example.savatteri_euris.queue;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.savatteri_euris.models.aggs.AggProduct;
import com.example.savatteri_euris.models.events.OrderProduct;
import com.example.savatteri_euris.models.events.Orders;
import com.example.savatteri_euris.models.queues.QueueOrdersModified;
import com.example.savatteri_euris.models.queues.QueueProductModified;
import com.example.savatteri_euris.services.AggProductService;
import com.example.savatteri_euris.services.OrdersService;
import com.example.savatteri_euris.services.QueueOrdersModifiedService;
import com.example.savatteri_euris.services.QueueProductModifiedService;
import com.example.savatteri_euris.utils.OrderUtil;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Service
@Getter
@Slf4j
@ConditionalOnProperty(name = QueueProductModifiedConsumer.PARAM_ENABLED)
public class QueueProductModifiedConsumer {

	public static final String PARAM_ENABLED = "queue.QueueProductModifiedConsumer.enabled";
	public static final String PARAM_CRON = "${queue.QueueProductModifiedConsumer.cron:0/30 * * * * *}";

	@Autowired
	QueueProductModifiedService queueProductModifiedService;
	@Autowired
	QueueOrdersModifiedService queueOrdersModifiedService;

	@Autowired
	OrdersService ordersService;
	@Autowired
	AggProductService aggProductService;

	@Transactional
	@Scheduled(cron = PARAM_CRON, zone = "Europe/Rome")
	public void process() {

		log.info("start");
		QueueProductModified queueElement = getQueueProductModifiedService().findFirstUnlocked();

		if (queueElement != null) {
			if (tryToLock(queueElement.getId())) {
				String eventCode = queueElement.getEventCode();
				Orders orders = getOrdersService().findFirstByEventCodeAndStatus(eventCode, OrderUtil.STATUS_WAITING);
				List<OrderProduct> orderProducts = orders.getOrderProducts();

				orderProducts.forEach(orderProduct -> {
					Integer quantity = orderProduct.getQuantity();
					String productCode = orderProduct.getProduct().getCode();
					AggProduct aggProduct = getAggProductService().findOneByCode(productCode);
					boolean result = getAggProductService().decreaseStock(aggProduct.getId(), quantity);
					if (result) {
						setProductCodeOrderedByStatus(OrderUtil.STATUS_ORDERED, orders, orderProduct);
					} else {
						setProductCodeOrderedByStatus(OrderUtil.STATUS_REJECTED, orders, orderProduct);
					}
				});
				getQueueProductModifiedService().deleteQueueElement(queueElement);
			} else {
				log.warn("unable to acquire lock for id={}", queueElement.getId());
			}
		}
	}

	private void setProductCodeOrderedByStatus(String status, Orders orders, OrderProduct orderProduct) {

		List<OrderProduct> orderProducts = new ArrayList<>();
		Orders ordersUpdate = new Orders();
		ordersUpdate.setInsertDate(new Date());
		ordersUpdate.setStatus(status);
		ordersUpdate.setEventCode(orders.getEventCode());
		ordersUpdate.setCustomer(orders.getCustomer());

		OrderProduct newOrderProduct = new OrderProduct();
		newOrderProduct.setQuantity(orderProduct.getQuantity());
		newOrderProduct.setProduct(orderProduct.getProduct());

		newOrderProduct.setOrders(ordersUpdate);

		orderProducts.add(newOrderProduct);
		log.info("orderProducts size={}", orderProducts.size());

		ordersUpdate.setOrderProducts(orderProducts);

		getOrdersService().save(ordersUpdate);
		addOrdersToQueue(orders.getEventCode());

	}

	private void addOrdersToQueue(String eventCode) {

		QueueOrdersModified queueOrdersModified = new QueueOrdersModified();
		queueOrdersModified.setEventCode(eventCode);
		queueOrdersModified.setInsertDate(new Date());
		queueOrdersModified.setLock(false);
		getQueueOrdersModifiedService().save(queueOrdersModified);
	}

	public boolean tryToLock(Long id) {
		return getQueueProductModifiedService().tryToLock(id);
	}
}
