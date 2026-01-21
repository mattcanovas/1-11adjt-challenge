package br.com.fiap.gfood.api.presentation.models;

import jakarta.validation.constraints.NotBlank;

public record SignInRequest(@NotBlank(message = "The login must be informed.") String login,
		@NotBlank(message = "The password must be informed.") String password)
{

}
