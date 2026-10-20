package com.davidbarrera.supportorders.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.davidbarrera.supportorders.dto.CreateSupportOrderRequest;
import com.davidbarrera.supportorders.dto.SupportOrderResponse;
import com.davidbarrera.supportorders.dto.SupportOrderSummaryResponse;
import com.davidbarrera.supportorders.dto.UpdateOrderStatusRequest;
import com.davidbarrera.supportorders.entity.SupportOrder;
import com.davidbarrera.supportorders.enums.OrderStatus;
import com.davidbarrera.supportorders.exception.DuplicateActiveOrderException;
import com.davidbarrera.supportorders.exception.InvalidStatusTransitionException;
import com.davidbarrera.supportorders.exception.OrderNotFoundException;
import com.davidbarrera.supportorders.factory.SupportOrderFactory;
import com.davidbarrera.supportorders.repository.SupportOrderRepository;

@Service
public class SupportOrderService {

	private static final Logger LOGGER = LoggerFactory.getLogger(SupportOrderService.class);

	private static final List<OrderStatus> ACTIVE_STATUSES = List.of(OrderStatus.OPEN, OrderStatus.IN_PROGRESS);

	private final SupportOrderRepository repository;
	private final SupportOrderFactory factory;

	public SupportOrderService(SupportOrderRepository repository, SupportOrderFactory factory) {

		this.repository = repository;
		this.factory = factory;
	}

	@Transactional
	public SupportOrderResponse create(CreateSupportOrderRequest request) {

		boolean activeOrderExists = repository.existsByExternalReferenceAndStatusIn(request.externalReference(),
				ACTIVE_STATUSES);

		if (activeOrderExists) {
			throw new DuplicateActiveOrderException(request.externalReference());
		}

		SupportOrder order = factory.create(request);

		SupportOrder savedOrder = repository.save(order);

		LOGGER.info("Support order created. id={}, externalReference={}", savedOrder.getId(),
				savedOrder.getExternalReference());

		return toResponse(savedOrder);
	}

	@Transactional(readOnly = true)
	public List<SupportOrderSummaryResponse> findAll(OrderStatus status) {

		List<SupportOrder> orders;

		if (status == null) {
			orders = repository.findAll();
		} else {
			orders = repository.findByStatus(status);
		}

		return orders.stream().map(this::toSummaryResponse).toList();
	}

	@Transactional(readOnly = true)
	public SupportOrderResponse findById(Long id) {

		SupportOrder order = repository.findById(id).orElseThrow(() -> new OrderNotFoundException(id));

		return toResponse(order);
	}

	@Transactional
	public SupportOrderResponse updateStatus(Long id, UpdateOrderStatusRequest request) {

		SupportOrder order = repository.findById(id).orElseThrow(() -> new OrderNotFoundException(id));

		OrderStatus currentStatus = order.getStatus();

		OrderStatus targetStatus = request.status();

		validateTransition(currentStatus, targetStatus);

		order.changeStatus(targetStatus);

		SupportOrder updatedOrder = repository.save(order);

		LOGGER.info("Support order status changed. id={}, from={}, to={}", id, currentStatus, targetStatus);

		return toResponse(updatedOrder);
	}

	private void validateTransition(OrderStatus currentStatus, OrderStatus targetStatus) {

		boolean validTransition = switch (currentStatus) {

		case OPEN -> targetStatus == OrderStatus.IN_PROGRESS || targetStatus == OrderStatus.CANCELLED;

		case IN_PROGRESS -> targetStatus == OrderStatus.RESOLVED || targetStatus == OrderStatus.CANCELLED;

		case RESOLVED, CANCELLED -> false;
		};

		if (!validTransition) {
			throw new InvalidStatusTransitionException(currentStatus, targetStatus);
		}
	}

	private SupportOrderResponse toResponse(SupportOrder order) {

		return new SupportOrderResponse(order.getId(), order.getExternalReference(), order.getCustomerId(),
				order.getCategory(), order.getDescription(), order.getChannel(), order.getPriority(), order.getStatus(),
				order.getCreatedAt());
	}

	private SupportOrderSummaryResponse toSummaryResponse(SupportOrder order) {

		return new SupportOrderSummaryResponse(order.getId(), order.getExternalReference(), order.getCategory(),
				order.getPriority(), order.getStatus(), order.getCreatedAt());
	}
}