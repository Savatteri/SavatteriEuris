package com.example.savatteri_euris.queue;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.savatteri_euris.models.aggs.AggOrders;
import com.example.savatteri_euris.models.aggs.AggProduct;
import com.example.savatteri_euris.models.events.OrderProduct;
import com.example.savatteri_euris.models.events.Orders;
import com.example.savatteri_euris.models.queues.QueueOrdersModified;
import com.example.savatteri_euris.models.queues.QueueProductModified;
import com.example.savatteri_euris.services.AggOrdersService;
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
@ConditionalOnProperty(name = QueueOrdersModifiedConsumer.PARAM_ENABLED)
public class QueueOrdersModifiedConsumer {

	public static final String PARAM_ENABLED = "queue.QueueOrdersModifiedConsumer.enabled";
	public static final String PARAM_CRON = "${queue.QueueOrdersModifiedConsumer.cron:0/30 * * * * *}";

	@Autowired
	QueueOrdersModifiedService queueOrdersModifiedService;
	@Autowired
	OrdersService ordersService;
	@Autowired
	AggOrdersService aggOrdersService;

	@Transactional
	@Scheduled(cron = PARAM_CRON, zone = "Europe/Rome")
	public void process() {
		
		log.info("start");
		QueueOrdersModified queueElement = getQueueOrdersModifiedService().findFirstUnlocked();

		if (queueElement != null) {
			if (tryToLock(queueElement.getId())) {
				String eventCode = queueElement.getEventCode();
				
				List<Orders> orders = getOrdersService().findByEventCode(eventCode);
				orders.forEach(order ->{
					List<OrderProduct> orderProducts = order.getOrderProducts();
					orderProducts.forEach(orderProduct -> {
						
						long productId = orderProduct.getProduct().getId();
						
						AggOrders aggOrders = getAggOrdersService().findOneByEventCodeAndProductId(eventCode, productId);
						if(aggOrders == null) {
							initAggOrders(order, orderProduct);
						}
						else {
							updateAggOrders(order, orderProduct, aggOrders);
						}
							
					});
					
				});

				
				getQueueOrdersModifiedService().deleteQueueElement(queueElement);
			} else {
				log.warn("unable to acquire lock for id={}", queueElement.getId());
			}
		}
	}

	private void updateAggOrders(Orders order, OrderProduct orderProduct, AggOrders aggOrders) {
		getAggOrdersService().updateByOrders(order, orderProduct, aggOrders);
		
	}

	private void initAggOrders( Orders order, OrderProduct orderProduct) {
		getAggOrdersService().initByOrders(order, orderProduct);
		
	}
	

	private void setProductCodeOrderedByStatus(String status, Orders orders) {

		Orders ordersUpdate = new Orders();
		ordersUpdate.setInsertDate(new Date());
		ordersUpdate.setStatus(status);
		ordersUpdate.setEventCode(orders.getEventCode());
		ordersUpdate.setCustomer(orders.getCustomer());
		
		getOrdersService().save(ordersUpdate);

	}

	private boolean tryToLock(Long id) {
		return getQueueOrdersModifiedService().tryToLock(id);
	}
}
