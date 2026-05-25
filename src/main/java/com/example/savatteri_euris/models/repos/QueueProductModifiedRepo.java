package com.example.savatteri_euris.models.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.example.savatteri_euris.models.queues.QueueProductModified;

public interface QueueProductModifiedRepo extends JpaRepository<QueueProductModified, Long> {

	public QueueProductModified findOneById(Long id);

	public QueueProductModified findFirstByLock(boolean lock);

	public QueueProductModified findOneByEventCode(String eventCode);

	@Transactional
	@Modifying
	@Query("UPDATE QueueProductModified q SET q.lock = true WHERE q.lock = false AND q.id = :id")
	int lockQueueItem(@Param("id") Long id);

}
