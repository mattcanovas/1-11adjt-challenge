package br.com.fiap.gfood.api.application.usecase;

import static org.apache.commons.lang3.StringUtils.isBlank;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.fiap.gfood.api.domain.exception.TypeUserNotFoundException;
import br.com.fiap.gfood.api.domain.gateway.TypeUserGateway;
import br.com.fiap.gfood.api.domain.model.TypeUser;

@Service
public class FindTypeUsersUseCase
{
	private static final String MESSAGE_ERROR_TYPE_USER_NOT_FOUND = "The type user requested was not found.";

	private final TypeUserGateway gateway;

	public FindTypeUsersUseCase(TypeUserGateway gateway)
	{
		this.gateway = gateway;
	}

	public Page<TypeUser> execute(String name, Pageable pageable)
	{
		return isBlank(name)
				? gateway.findAll(pageable)
				: gateway.findByNameContaining(name, pageable);
	}

	public TypeUser executeById(UUID id)
	{
		return gateway.findById(id)
				.orElseThrow(() -> new TypeUserNotFoundException(MESSAGE_ERROR_TYPE_USER_NOT_FOUND));
	}
}
