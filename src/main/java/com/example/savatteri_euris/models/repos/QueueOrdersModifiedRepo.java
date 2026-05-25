package com.example.savatteri_euris.models.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.example.savatteri_euris.models.facts.Customer;
import com.example.savatteri_euris.models.facts.Product;
import com.example.savatteri_euris.models.queues.QueueOrdersModified;
import com.example.savatteri_euris.models.queues.QueueProductModified;


public interface QueueOrdersModifiedRepo extends JpaRepository<QueueOrdersModified, Long> {
	public QueueOrdersModified findOneById(Long id);
	public QueueOrdersModified findFirstByLock(boolean lock);
	public QueueOrdersModified findOneByEventCode(String eventCode);

	
	@Transactional
    @Modifying
    @Query("UPDATE QueueOrdersModified q SET q.lock = true WHERE q.lock = false AND q.id = :id")
    int lockQueueItem(@Param("id") Long id);
	
	

}
