package br.com.fiap.gfood.api.application.dto;

import br.com.fiap.gfood.api.domain.enums.TypeKitchen;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record CreateRestaurantRequest(

        @NotBlank(message = "The name must be informed.")
        @Size(min = 2, max = 255, message = "The name must be between 2 and 255 characters.")
        String name,

        @NotBlank(message = "The address must be informed.")
        @Size(min = 5, max = 255, message = "The address must be between 5 and 255 characters.")
        String address,

        @NotNull(message = "The type of kitchen must be informed.")
        TypeKitchen typeKitchen,

        @NotBlank(message = "The opening hours must be informed.")
        @Pattern(regexp = "^([01]\\d|2[0-3]):[0-5]\\d - ([01]\\d|2[0-3]):[0-5]\\d$",
                message = "The opening hours must follow the format HH:mm - HH:mm.")
        String openingHours,

        @NotNull(message = "The owner id must be informed.")
        UUID ownerId
) {
}
