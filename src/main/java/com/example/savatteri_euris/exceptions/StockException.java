package com.example.savatteri_euris.exceptions;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class StockException extends RuntimeException {

	private static final long serialVersionUID = -4584347985027200380L;

	public StockException() {
		super();
	}
	
	public StockException(String message) {
		super(message);
	}
	
	public StockException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public StockException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
	
}
