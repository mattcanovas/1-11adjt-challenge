package br.com.fiap.gfood.api.application.usecase;

import java.util.UUID;

import org.springframework.stereotype.Service;

import br.com.fiap.gfood.api.application.dto.UpdateTypeUserRequest;
import br.com.fiap.gfood.api.domain.exception.TypeUserNameAlreadyExistsException;
import br.com.fiap.gfood.api.domain.exception.TypeUserNotFoundException;
import br.com.fiap.gfood.api.domain.gateway.TypeUserGateway;
import br.com.fiap.gfood.api.domain.model.TypeUser;

@Service
public class UpdateTypeUserUseCase
{
	private static final String MESSAGE_ERROR_TYPE_USER_NOT_FOUND = "The type user requested was not found.";
	private static final String MESSAGE_ERROR_NAME_ALREADY_EXISTS = "Type user name is already in use.";

	private final TypeUserGateway gateway;

	public UpdateTypeUserUseCase(TypeUserGateway gateway)
	{
		this.gateway = gateway;
	}

	public TypeUser execute(UUID id, UpdateTypeUserRequest request)
	{
		TypeUser typeUser = gateway.findById(id)
				.orElseThrow(() -> new TypeUserNotFoundException(MESSAGE_ERROR_TYPE_USER_NOT_FOUND));

		if (!typeUser.getName().equals(request.name()) && gateway.existsByName(request.name()))
		{
			throw new TypeUserNameAlreadyExistsException(MESSAGE_ERROR_NAME_ALREADY_EXISTS);
		}

		typeUser.setName(request.name());
		typeUser.setDescription(request.description());

		return gateway.save(typeUser);
	}
}
