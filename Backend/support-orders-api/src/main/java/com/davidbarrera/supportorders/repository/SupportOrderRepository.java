package com.davidbarrera.supportorders.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.davidbarrera.supportorders.entity.SupportOrder;
import com.davidbarrera.supportorders.enums.OrderStatus;

public interface SupportOrderRepository extends JpaRepository<SupportOrder, Long> {

	boolean existsByExternalReferenceAndStatusIn(String externalReference, Collection<OrderStatus> statuses);

	List<SupportOrder> findByStatus(OrderStatus status);
}