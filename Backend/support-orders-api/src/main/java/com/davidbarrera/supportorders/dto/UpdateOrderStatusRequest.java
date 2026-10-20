package com.davidbarrera.supportorders.dto;

import com.davidbarrera.supportorders.enums.OrderStatus;

import jakarta.validation.constraints.NotNull;

public record UpdateOrderStatusRequest(
		
		@NotNull(message = "status is required") 
		OrderStatus status) 
{
}
