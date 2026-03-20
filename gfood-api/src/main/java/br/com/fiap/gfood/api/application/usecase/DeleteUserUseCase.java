package br.com.fiap.gfood.api.application.usecase;

import static java.util.concurrent.CompletableFuture.runAsync;

import java.util.UUID;

import org.springframework.stereotype.Service;

import br.com.fiap.gfood.api.domain.gateway.UserGateway;

@Service
public class DeleteUserUseCase
{
	private final UserGateway gateway;

	public DeleteUserUseCase(UserGateway gateway)
	{
		this.gateway = gateway;
	}

	public void execute(UUID id)
	{
		runAsync(() -> gateway.deleteById(id));
	}
}
