package com.davidbarrera.supportorders.dto;

import com.davidbarrera.supportorders.enums.Priority;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateSupportOrderRequest(

        @NotBlank(message = "externalReference is required")
        String externalReference,

        @NotBlank(message = "customerId is required")
        String customerId,

        @NotBlank(message = "category is required")
        String category,

        @NotBlank(message = "description is required")
        @Size(
                min = 10,
                max = 500,
                message = "description must contain between 10 and 500 characters"
        )
        String description,

        @NotBlank(message = "channel is required")
        String channel,

        Priority priority

) {
}