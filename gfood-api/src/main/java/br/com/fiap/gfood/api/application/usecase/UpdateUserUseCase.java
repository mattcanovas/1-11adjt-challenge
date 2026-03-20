package br.com.fiap.gfood.api.application.usecase;

import static org.apache.commons.lang3.StringUtils.isBlank;

import java.util.UUID;

import org.springframework.stereotype.Service;

import br.com.fiap.gfood.api.application.dto.UpdateUserRequest;
import br.com.fiap.gfood.api.domain.exception.EmailAlreadyUsedException;
import br.com.fiap.gfood.api.domain.exception.TypeUserNotFoundException;
import br.com.fiap.gfood.api.domain.exception.UserNotFoundException;
import br.com.fiap.gfood.api.domain.gateway.TypeUserGateway;
import br.com.fiap.gfood.api.domain.gateway.UserGateway;
import br.com.fiap.gfood.api.domain.model.TypeUser;
import br.com.fiap.gfood.api.domain.model.User;

@Service
public class UpdateUserUseCase
{
	private static final String MESSAGE_ERROR_USER_NOT_FOUND = "The user requested was not found.";
	private static final String MESSAGE_ERROR_EMAIL_IS_ALREADY_USED = "Email is already used.";
	private static final String MESSAGE_ERROR_TYPE_USER_NOT_FOUND = "The type user informed was not found.";

	private final UserGateway userGateway;
	private final TypeUserGateway typeUserGateway;

	public UpdateUserUseCase(UserGateway userGateway, TypeUserGateway typeUserGateway)
	{
		this.userGateway = userGateway;
		this.typeUserGateway = typeUserGateway;
	}

	public User execute(UUID id, UpdateUserRequest request)
	{
		User user = userGateway.findById(id)
				.orElseThrow(() -> new UserNotFoundException(MESSAGE_ERROR_USER_NOT_FOUND));

		if (userGateway.existsByEmail(request.email()))
		{
			throw new EmailAlreadyUsedException(MESSAGE_ERROR_EMAIL_IS_ALREADY_USED);
		}

		TypeUser typeUser = typeUserGateway.findById(request.typeUserId())
				.orElseThrow(() -> new TypeUserNotFoundException(MESSAGE_ERROR_TYPE_USER_NOT_FOUND));

		user.setFullName(isBlank(request.fullName()) ? user.getFullName() : request.fullName());
		user.setEmail(isBlank(request.email()) ? user.getEmail() : request.email());
		user.setLogin(isBlank(request.login()) ? user.getLogin() : request.login());
		user.setTypeUser(typeUser);
		user.setAddress(isBlank(request.address()) ? user.getAddress() : request.address());

		return userGateway.save(user);
	}
}
