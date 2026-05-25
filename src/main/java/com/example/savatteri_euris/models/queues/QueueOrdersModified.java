package com.example.savatteri_euris.models.queues;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.example.savatteri_euris.models.queues.base.BaseQueue;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
@ToString
public class QueueOrdersModified extends BaseQueue {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String eventCode;
	private long productId;

}
