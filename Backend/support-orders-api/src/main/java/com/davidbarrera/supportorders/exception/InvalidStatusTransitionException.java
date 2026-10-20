package com.davidbarrera.supportorders.exception;

import com.davidbarrera.supportorders.enums.OrderStatus;

public class InvalidStatusTransitionException extends RuntimeException {

	public InvalidStatusTransitionException(OrderStatus currentStatus, OrderStatus targetStatus) {

		super("Transition from " + currentStatus + " to " + targetStatus + " is not allowed");
	}

}
