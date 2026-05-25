package com.example.savatteri_euris.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.savatteri_euris.models.queues.QueueOrdersModified;
import com.example.savatteri_euris.models.repos.QueueOrdersModifiedRepo;

@Service
public class QueueOrdersModifiedService {

	@Autowired
	QueueOrdersModifiedRepo queueOrdersModifiedRepo;

	public void save(QueueOrdersModified queueProductModified) {
		queueOrdersModifiedRepo.save(queueProductModified);
	}

	public QueueOrdersModified findFirstUnlocked() {
		return queueOrdersModifiedRepo.findFirstByLock(false);
	}

	public boolean tryToLock(Long id) {
		int result = queueOrdersModifiedRepo.lockQueueItem(id);
		return result > 0 ? true : false;
	}

	public void deleteQueueElement(QueueOrdersModified queueOrdersModified) {
		queueOrdersModifiedRepo.delete(queueOrdersModified);
	}

	public QueueOrdersModified findOneByEventCode(String eventCode) {
		return queueOrdersModifiedRepo.findOneByEventCode(eventCode);
	}

}
