package br.com.fiap.gfood.api.application.usecase;

import org.springframework.stereotype.Service;

import br.com.fiap.gfood.api.application.dto.CreateUserRequest;
import br.com.fiap.gfood.api.domain.exception.EmailAlreadyUsedException;
import br.com.fiap.gfood.api.domain.gateway.UserGateway;
import br.com.fiap.gfood.api.domain.model.User;

@Service
public class CreateUserUseCase
{
	private static final String MESSAGE_ERROR_EMAIL_IS_ALREADY_USED = "Email is already used.";

	private final UserGateway gateway;

	public CreateUserUseCase(UserGateway gateway)
	{
		this.gateway = gateway;
	}

	public User execute(CreateUserRequest request)
	{
		if (gateway.existsByEmail(request.email()))
		{
			throw new EmailAlreadyUsedException(MESSAGE_ERROR_EMAIL_IS_ALREADY_USED);
		}
		User user = User.builder()
				.fullName(request.fullName())
				.email(request.email())
				.login(request.login())
				.password(request.password())
				.type(request.type())
				.address(request.address())
				.build();
		return gateway.save(user);
	}
}
