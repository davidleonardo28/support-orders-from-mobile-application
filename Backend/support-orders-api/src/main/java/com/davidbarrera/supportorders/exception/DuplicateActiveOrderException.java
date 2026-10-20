package com.davidbarrera.supportorders.exception;

public class DuplicateActiveOrderException extends RuntimeException {

	public DuplicateActiveOrderException(String externalReference) {

		super("An active support order already exists for externalReference: " + externalReference);
	}
}
