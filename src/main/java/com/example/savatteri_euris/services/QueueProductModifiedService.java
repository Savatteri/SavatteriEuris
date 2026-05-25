package com.example.savatteri_euris.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.savatteri_euris.models.queues.QueueProductModified;
import com.example.savatteri_euris.models.repos.QueueProductModifiedRepo;

@Service
public class QueueProductModifiedService {

	@Autowired
	QueueProductModifiedRepo queueProductModifiedRepo;

	public void save(QueueProductModified queueProductModified) {
		queueProductModifiedRepo.save(queueProductModified);
	}

	public QueueProductModified findFirstUnlocked() {
		return queueProductModifiedRepo.findFirstByLock(false);
	}

	public boolean tryToLock(Long id) {
		int result = queueProductModifiedRepo.lockQueueItem(id);
		return result > 0 ? true : false;
	}

	public void deleteQueueElement(QueueProductModified queueProductModified) {
		queueProductModifiedRepo.delete(queueProductModified);
	}

	public QueueProductModified findOneByEventCode(String eventCode) {
		return queueProductModifiedRepo.findOneByEventCode(eventCode);
	}

}
