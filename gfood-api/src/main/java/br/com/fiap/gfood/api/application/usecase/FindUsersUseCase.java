package br.com.fiap.gfood.api.application.usecase;

import static org.apache.commons.lang3.StringUtils.isBlank;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.fiap.gfood.api.domain.gateway.UserGateway;
import br.com.fiap.gfood.api.domain.model.User;

@Service
public class FindUsersUseCase
{
	private final UserGateway gateway;

	public FindUsersUseCase(UserGateway gateway)
	{
		this.gateway = gateway;
	}

	public Page<User> execute(String fullName, Pageable pageable)
	{
		return isBlank(fullName)
				? gateway.findAll(pageable)
				: gateway.findByFullNameContaining(fullName, pageable);
	}
}
