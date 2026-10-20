package com.davidbarrera.supportorders.factory;

import org.springframework.stereotype.Component;

import com.davidbarrera.supportorders.dto.CreateSupportOrderRequest;
import com.davidbarrera.supportorders.entity.SupportOrder;

@Component
public class SupportOrderFactory {
	 public SupportOrder create(CreateSupportOrderRequest request) {

	        return new SupportOrder(
	                request.externalReference(),
	                request.customerId(),
	                request.category(),
	                request.description(),
	                request.channel(),
	                request.priority()
	        );
	    }

}
