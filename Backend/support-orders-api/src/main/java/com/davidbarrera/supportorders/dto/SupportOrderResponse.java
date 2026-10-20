package com.davidbarrera.supportorders.dto;

import java.time.LocalDateTime;

import com.davidbarrera.supportorders.enums.OrderStatus;
import com.davidbarrera.supportorders.enums.Priority;

public record SupportOrderResponse(
		 Long id,
	     String externalReference,
	     String customerId,
	     String category,
	     String description,
	     String channel,
	     Priority priority,
	     OrderStatus status,
	     LocalDateTime createdAt) {
}
