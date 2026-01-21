package br.com.fiap.gfood.api.presentation.models;

import br.com.fiap.gfood.api.core.commons.enums.TypeCustomer;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateCustomerRequest(
		@NotBlank(message = "The full name must be informed.") @Size(min = 3, max = 50, message = "The full name length must contains a minimum of {min} and maximum of {max} characters.") String fullName,
		@NotBlank(message = "The email must be informed") @Email(message = "The email is invalid.") String email,
		@NotBlank(message = "The login must be informed.") @Size(min = 3, max = 50, message = "The login length must contains a minimum of {min} and maximum of {max} characters.") String login,
		@NotBlank(message = "The password must be informed.") @Size(min = 6, message = "The password length ust contains a minimum of {min} characters.") String password,
		@NotNull(message = "The type must be informed.") TypeCustomer type)

{

}
