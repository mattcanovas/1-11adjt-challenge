package br.com.fiap.gfood.api.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateTypeUserRequest(
		@NotBlank(message = "The name must be informed.") @Size(min = 2, max = 50, message = "The name length must contain a minimum of {min} and maximum of {max} characters.") String name,
		@Size(max = 255, message = "The description length must contain a maximum of {max} characters.") String description)
{
}
