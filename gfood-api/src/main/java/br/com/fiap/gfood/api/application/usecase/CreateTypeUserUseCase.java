package br.com.fiap.gfood.api.application.usecase;

import org.springframework.stereotype.Service;

import br.com.fiap.gfood.api.application.dto.CreateTypeUserRequest;
import br.com.fiap.gfood.api.domain.exception.TypeUserNameAlreadyExistsException;
import br.com.fiap.gfood.api.domain.gateway.TypeUserGateway;
import br.com.fiap.gfood.api.domain.model.TypeUser;

@Service
public class CreateTypeUserUseCase
{
	private static final String MESSAGE_ERROR_NAME_ALREADY_EXISTS = "Type user name is already in use.";

	private final TypeUserGateway gateway;

	public CreateTypeUserUseCase(TypeUserGateway gateway)
	{
		this.gateway = gateway;
	}

	public TypeUser execute(CreateTypeUserRequest request)
	{
		if (gateway.existsByName(request.name()))
		{
			throw new TypeUserNameAlreadyExistsException(MESSAGE_ERROR_NAME_ALREADY_EXISTS);
		}
		TypeUser typeUser = TypeUser.builder()
				.name(request.name())
				.description(request.description())
				.build();
		return gateway.save(typeUser);
	}
}
