package com.davidbarrera.supportorders.exception;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
	    @ExceptionHandler(DuplicateActiveOrderException.class)
	    public ResponseEntity<Map<String, String>> handleDuplicateActiveOrder(
	            DuplicateActiveOrderException exception) {

	        return ResponseEntity
	                .status(HttpStatus.CONFLICT)
	                .body(Map.of(
	                        "error", "Conflict",
	                        "message", exception.getMessage()
	                ));
	    }

	    @ExceptionHandler(InvalidStatusTransitionException.class)
	    public ResponseEntity<Map<String, String>> handleInvalidStatusTransition(
	            InvalidStatusTransitionException exception) {

	        return ResponseEntity
	                .status(HttpStatus.CONFLICT)
	                .body(Map.of(
	                        "error", "Conflict",
	                        "message", exception.getMessage()
	                ));
	    }

	    @ExceptionHandler(OrderNotFoundException.class)
	    public ResponseEntity<Map<String, String>> handleOrderNotFound(
	            OrderNotFoundException exception) {

	        return ResponseEntity
	                .status(HttpStatus.NOT_FOUND)
	                .body(Map.of(
	                        "error", "Not Found",
	                        "message", exception.getMessage()
	                ));
	    }
	}
