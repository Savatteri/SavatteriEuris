package com.example.savatteri_euris.models.queues.base;

import java.util.Date;

import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@MappedSuperclass
public class BaseQueue {

	private Date insertDate;
	private boolean lock;

}
