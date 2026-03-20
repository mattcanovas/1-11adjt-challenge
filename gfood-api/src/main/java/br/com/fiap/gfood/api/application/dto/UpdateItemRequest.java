package br.com.fiap.gfood.api.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record UpdateItemRequest(

        @NotBlank(message = "The name must be informed.")
        @Size(min = 2, max = 255, message = "The name must be between 2 and 255 characters.")
        String name,

        @NotBlank(message = "The description must be informed.")
        String description,

        @NotNull(message = "The price must be informed.")
        @Positive(message = "The price must be a positive value.")
        BigDecimal price,

        @NotNull(message = "The availability to order local must be informed.")
        Boolean availabilityToOrderLocal,

        @Size(max = 255, message = "The photo path must be at most 255 characters.")
        String photo
) {
}
