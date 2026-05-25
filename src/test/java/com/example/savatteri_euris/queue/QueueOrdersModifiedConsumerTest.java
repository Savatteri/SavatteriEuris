package com.example.savatteri_euris.queue;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.example.savatteri_euris.models.queues.QueueOrdersModified;
import com.example.savatteri_euris.models.repos.QueueOrdersModifiedRepo;
import com.example.savatteri_euris.services.ProductService;
import com.example.savatteri_euris.services.QueueOrdersModifiedService;
import com.example.savatteri_euris.utils.OrderUtil;

import lombok.Getter;

@ActiveProfiles("test")
@Getter
@SpringBootTest
public class QueueOrdersModifiedConsumerTest {

	@Autowired
	QueueOrdersModifiedConsumer queueOrdersModifiedConsumer;
	@Autowired
	QueueOrdersModifiedService queueOrdersModifiedService;
	@Autowired
	QueueOrdersModifiedRepo queueOrdersModifiedRepo;
	
	@BeforeEach
	private void deleteAll() {
		queueOrdersModifiedRepo.deleteAll();
	}
	
	@Test
	void shouldTryToLock() {
		
		QueueOrdersModified queue = new QueueOrdersModified();
		queue.setEventCode("eventCode");
		queue.setLock(false);
		queue.setProductId(1L);
		
		getQueueOrdersModifiedService().save(queue);
		
		QueueOrdersModified queueFromDb = getQueueOrdersModifiedService().findOneByEventCode("eventCode");
		
		
		 boolean result = getQueueOrdersModifiedConsumer().tryToLock(queueFromDb.getId());
		 
	     assertEquals(true,result);
	     
	}
	
	@Test
	void shouldNotTryToLock() {
		
		QueueOrdersModified queue = new QueueOrdersModified();
		queue.setEventCode("eventCode");
		queue.setLock(true);
		queue.setProductId(1L);
		
		getQueueOrdersModifiedService().save(queue);
		
		QueueOrdersModified queueFromDb = getQueueOrdersModifiedService().findOneByEventCode("eventCode");
		
		boolean result = getQueueOrdersModifiedConsumer().tryToLock(queueFromDb.getId());
		 
	    assertEquals(false,result);
	     
	}
	
}
