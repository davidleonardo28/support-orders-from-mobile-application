package com.davidbarrera.supportorders.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PatchMapping;
import com.davidbarrera.supportorders.dto.CreateSupportOrderRequest;
import com.davidbarrera.supportorders.dto.SupportOrderResponse;
import com.davidbarrera.supportorders.enums.OrderStatus;
import com.davidbarrera.supportorders.service.SupportOrderService;
import com.davidbarrera.supportorders.dto.SupportOrderSummaryResponse;
import com.davidbarrera.supportorders.dto.UpdateOrderStatusRequest;

import org.springframework.web.bind.annotation.PathVariable;
import jakarta.validation.Valid;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/support-orders")
public class SupportOrderController {

	private final SupportOrderService service;

	public SupportOrderController(SupportOrderService service) {
		this.service = service;
	}

	@PostMapping
	public ResponseEntity<SupportOrderResponse> create(@Valid @RequestBody CreateSupportOrderRequest request) {

		SupportOrderResponse response = service.create(request);

		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@GetMapping
	public ResponseEntity<List<SupportOrderSummaryResponse>> findAll(
			@RequestParam(required = false) OrderStatus status) {

		List<SupportOrderSummaryResponse> response = service.findAll(status);

		return ResponseEntity.ok(response);
	}

	@GetMapping("/{id}")
	public ResponseEntity<SupportOrderResponse> findById(@PathVariable Long id) {

		SupportOrderResponse response = service.findById(id);

		return ResponseEntity.ok(response);
	}

	@PatchMapping("/{id}/status")
	public ResponseEntity<SupportOrderResponse> updateStatus(@PathVariable Long id,
			@Valid @RequestBody UpdateOrderStatusRequest request) {

		SupportOrderResponse response = service.updateStatus(id, request);

		return ResponseEntity.ok(response);
	}

}