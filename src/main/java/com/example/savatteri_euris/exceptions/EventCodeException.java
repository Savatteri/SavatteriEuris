package com.example.savatteri_euris.exceptions;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class EventCodeException extends RuntimeException {

	private static final long serialVersionUID = 693951099808877571L;

	public EventCodeException() {
		super();
	}
	
	public EventCodeException(String message) {
		super(message);
	}
	
	public EventCodeException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public EventCodeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
	
}
