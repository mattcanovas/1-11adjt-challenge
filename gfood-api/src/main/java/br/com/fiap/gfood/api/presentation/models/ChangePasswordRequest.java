package br.com.fiap.gfood.api.presentation.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;

public record ChangePasswordRequest(
		@NotBlank(message = "The old password must be informed.") @JsonProperty("old_password") String oldPassword,
		@NotBlank(message = "The new password must be informed.") @JsonProperty("new_password") String newPassword,
		@NotBlank(message = "The confirmation of the new password must be informed.") @JsonProperty("confirm_password") String confirmPassword)
{

}
