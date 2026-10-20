package com.davidbarrera.supportorders.dto;

import java.time.LocalDateTime;

import com.davidbarrera.supportorders.enums.OrderStatus;
import com.davidbarrera.supportorders.enums.Priority;

public record SupportOrderSummaryResponse(
		 Long id,
	     String externalReference,
	     String category,
	     Priority priority,
	     OrderStatus status,
	     LocalDateTime createdAt
		) {

}
