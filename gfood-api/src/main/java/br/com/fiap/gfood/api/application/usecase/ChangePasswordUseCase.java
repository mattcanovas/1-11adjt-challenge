package br.com.fiap.gfood.api.application.usecase;

import java.util.UUID;

import org.springframework.stereotype.Service;

import br.com.fiap.gfood.api.application.dto.ChangePasswordRequest;
import br.com.fiap.gfood.api.domain.exception.UserNotFoundException;
import br.com.fiap.gfood.api.domain.exception.PasswordConfirmationMismatchException;
import br.com.fiap.gfood.api.domain.exception.PasswordMismatchException;
import br.com.fiap.gfood.api.domain.gateway.UserGateway;
import br.com.fiap.gfood.api.domain.model.User;

@Service
public class ChangePasswordUseCase
{
	private static final String MESSAGE_ERROR_USER_NOT_FOUND = "The user requested was not found.";
	private static final String THE_OLD_PASSWORD_MISMATCH = "The old password informed mismatch";
	private static final String THE_CONFIRMATION_OF_NEW_PASSWORD_MISMATCH = "The confirmation password mismatched.";

	private final UserGateway gateway;

	public ChangePasswordUseCase(UserGateway gateway)
	{
		this.gateway = gateway;
	}

	public User execute(UUID id, ChangePasswordRequest request)
	{
		User user = gateway.findById(id)
				.orElseThrow(() -> new UserNotFoundException(MESSAGE_ERROR_USER_NOT_FOUND));

		if (!request.oldPassword().equals(user.getPassword()))
		{
			throw new PasswordMismatchException(THE_OLD_PASSWORD_MISMATCH);
		}
		if (!request.newPassword().equals(request.confirmPassword()))
		{
			throw new PasswordConfirmationMismatchException(THE_CONFIRMATION_OF_NEW_PASSWORD_MISMATCH);
		}

		user.setPassword(request.newPassword());
		return gateway.save(user);
	}
}
