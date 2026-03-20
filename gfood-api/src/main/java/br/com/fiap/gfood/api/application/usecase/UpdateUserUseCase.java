package br.com.fiap.gfood.api.application.usecase;

import static java.util.Objects.isNull;
import static org.apache.commons.lang3.StringUtils.isBlank;

import java.util.UUID;

import org.springframework.stereotype.Service;

import br.com.fiap.gfood.api.application.dto.UpdateUserRequest;
import br.com.fiap.gfood.api.domain.exception.UserNotFoundException;
import br.com.fiap.gfood.api.domain.exception.EmailAlreadyUsedException;
import br.com.fiap.gfood.api.domain.gateway.UserGateway;
import br.com.fiap.gfood.api.domain.model.User;

@Service
public class UpdateUserUseCase
{
	private static final String MESSAGE_ERROR_USER_NOT_FOUND = "The user requested was not found.";
	private static final String MESSAGE_ERROR_EMAIL_IS_ALREADY_USED = "Email is already used.";

	private final UserGateway gateway;

	public UpdateUserUseCase(UserGateway gateway)
	{
		this.gateway = gateway;
	}

	public User execute(UUID id, UpdateUserRequest request)
	{
		User user = gateway.findById(id)
				.orElseThrow(() -> new UserNotFoundException(MESSAGE_ERROR_USER_NOT_FOUND));

		if (gateway.existsByEmail(request.email()))
		{
			throw new EmailAlreadyUsedException(MESSAGE_ERROR_EMAIL_IS_ALREADY_USED);
		}

		user.setFullName(isBlank(request.fullName()) ? user.getFullName() : request.fullName());
		user.setEmail(isBlank(request.email()) ? user.getEmail() : request.email());
		user.setLogin(isBlank(request.login()) ? user.getLogin() : request.login());
		user.setType(isNull(request.type()) ? user.getType() : request.type());
		user.setAddress(isBlank(request.address()) ? user.getAddress() : request.address());

		return gateway.save(user);
	}
}
