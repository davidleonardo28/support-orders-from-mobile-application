package com.davidbarrera.supportorders.exception;

public class OrderNotFoundException extends RuntimeException {

	public OrderNotFoundException(Long id) {
		super("Support order not found with id: " + id);
	}

}
