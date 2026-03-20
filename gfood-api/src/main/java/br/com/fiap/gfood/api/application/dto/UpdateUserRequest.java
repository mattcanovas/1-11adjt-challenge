package br.com.fiap.gfood.api.application.dto;

import java.util.UUID;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UpdateUserRequest(
		@NotBlank(message = "The full name must be informed.") @Size(min = 3, max = 50, message = "The full name length must contains a minimum of {min} and maximum of {max} characters.") String fullName,
		@NotBlank(message = "The email must be informed") @Email(message = "The email is invalid.") String email,
		@NotBlank(message = "The login must be informed.") @Size(min = 3, max = 50, message = "The login length must contains a minimum of {min} and maximum of {max} characters.") String login,
		@NotNull(message = "The type user must be informed.") UUID typeUserId,
		@NotBlank(message = "The address must be informed.") @Size(min = 5, max = 50, message = "The address length must contain a minimum of {min} and a maximum {max} characters.") String address)
{
}
