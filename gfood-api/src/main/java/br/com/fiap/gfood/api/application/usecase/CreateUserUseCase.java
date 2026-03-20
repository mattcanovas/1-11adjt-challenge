package br.com.fiap.gfood.api.application.usecase;

import org.springframework.stereotype.Service;

import br.com.fiap.gfood.api.application.dto.CreateUserRequest;
import br.com.fiap.gfood.api.domain.exception.EmailAlreadyUsedException;
import br.com.fiap.gfood.api.domain.exception.TypeUserNotFoundException;
import br.com.fiap.gfood.api.domain.gateway.TypeUserGateway;
import br.com.fiap.gfood.api.domain.gateway.UserGateway;
import br.com.fiap.gfood.api.domain.model.TypeUser;
import br.com.fiap.gfood.api.domain.model.User;

@Service
public class CreateUserUseCase
{
	private static final String MESSAGE_ERROR_EMAIL_IS_ALREADY_USED = "Email is already used.";
	private static final String MESSAGE_ERROR_TYPE_USER_NOT_FOUND = "The type user informed was not found.";

	private final UserGateway userGateway;
	private final TypeUserGateway typeUserGateway;

	public CreateUserUseCase(UserGateway userGateway, TypeUserGateway typeUserGateway)
	{
		this.userGateway = userGateway;
		this.typeUserGateway = typeUserGateway;
	}

	public User execute(CreateUserRequest request)
	{
		if (userGateway.existsByEmail(request.email()))
		{
			throw new EmailAlreadyUsedException(MESSAGE_ERROR_EMAIL_IS_ALREADY_USED);
		}

		TypeUser typeUser = typeUserGateway.findById(request.typeUserId())
				.orElseThrow(() -> new TypeUserNotFoundException(MESSAGE_ERROR_TYPE_USER_NOT_FOUND));

		User user = User.builder()
				.fullName(request.fullName())
				.email(request.email())
				.login(request.login())
				.password(request.password())
				.typeUser(typeUser)
				.address(request.address())
				.build();
		return userGateway.save(user);
	}
}
