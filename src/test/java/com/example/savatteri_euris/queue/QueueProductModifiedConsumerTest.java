package com.example.savatteri_euris.queue;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.example.savatteri_euris.models.queues.QueueOrdersModified;
import com.example.savatteri_euris.models.queues.QueueProductModified;
import com.example.savatteri_euris.models.repos.QueueOrdersModifiedRepo;
import com.example.savatteri_euris.models.repos.QueueProductModifiedRepo;
import com.example.savatteri_euris.services.ProductService;
import com.example.savatteri_euris.services.QueueOrdersModifiedService;
import com.example.savatteri_euris.services.QueueProductModifiedService;
import com.example.savatteri_euris.utils.OrderUtil;

import lombok.Getter;

@ActiveProfiles("test")
@Getter
@SpringBootTest
public class QueueProductModifiedConsumerTest {

	@Autowired
	QueueProductModifiedConsumer queueProductModifiedConsumer;
	@Autowired
	QueueProductModifiedService queueProductModifiedService;
	@Autowired
	QueueProductModifiedRepo queueProductModifiedRepo;
	
	@BeforeEach
	private void deleteAll() {
		queueProductModifiedRepo.deleteAll();
	}
	
	@Test
	void shouldTryToLock() {
		
		QueueProductModified queue = new QueueProductModified();
		queue.setEventCode("eventCode");
		queue.setLock(false);
		
		getQueueProductModifiedService().save(queue);
		
		 QueueProductModified queueFromDb = getQueueProductModifiedService().findOneByEventCode("eventCode");
		
		
		 boolean result = getQueueProductModifiedConsumer().tryToLock(queueFromDb.getId());
		 
	     assertEquals(true,result);
	     
	}
	
	@Test
	void shouldNotTryToLock() {
		
		QueueProductModified queue = new QueueProductModified();
		queue.setEventCode("eventCode");
		queue.setLock(true);
		
		getQueueProductModifiedService().save(queue);
		
		 QueueProductModified queueFromDb = getQueueProductModifiedService().findOneByEventCode("eventCode");
		
		
		 boolean result = getQueueProductModifiedConsumer().tryToLock(queueFromDb.getId());
		 
	     assertEquals(false,result);
	     
	}
	
}
