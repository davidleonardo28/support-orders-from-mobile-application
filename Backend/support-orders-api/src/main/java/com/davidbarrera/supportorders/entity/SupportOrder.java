package com.davidbarrera.supportorders.entity;

import java.time.LocalDateTime;

import com.davidbarrera.supportorders.enums.OrderStatus;
import com.davidbarrera.supportorders.enums.Priority;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "support_orders")
public class SupportOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String externalReference;

    @Column(nullable = false, length = 100)
    private String customerId;

    @Column(nullable = false, length = 100)
    private String category;

    @Column(nullable = false, length = 500)
    private String description;

    @Column(nullable = false, length = 50)
    private String channel;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private Priority priority;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private OrderStatus status;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    protected SupportOrder() {
    }

    public SupportOrder(
            String externalReference,
            String customerId,
            String category,
            String description,
            String channel,
            Priority priority) {

        this.externalReference = externalReference;
        this.customerId = customerId;
        this.category = category;
        this.description = description;
        this.channel = channel;
        this.priority = priority;
        this.status = OrderStatus.OPEN;
    }

    @PrePersist
    public void prePersist() {

        if (status == null) {
            status = OrderStatus.OPEN;
        }

        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }

    public Long getId() {
        return id;
    }

    public String getExternalReference() {
        return externalReference;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public String getChannel() {
        return channel;
    }

    public Priority getPriority() {
        return priority;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void changeStatus(OrderStatus newStatus) {
        this.status = newStatus;
    }
}