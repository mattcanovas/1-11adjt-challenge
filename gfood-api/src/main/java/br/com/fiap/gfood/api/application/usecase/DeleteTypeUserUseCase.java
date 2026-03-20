package br.com.fiap.gfood.api.application.usecase;

import static java.util.concurrent.CompletableFuture.runAsync;

import java.util.UUID;

import org.springframework.stereotype.Service;

import br.com.fiap.gfood.api.domain.gateway.TypeUserGateway;

@Service
public class DeleteTypeUserUseCase
{
	private final TypeUserGateway gateway;

	public DeleteTypeUserUseCase(TypeUserGateway gateway)
	{
		this.gateway = gateway;
	}

	public void execute(UUID id)
	{
		runAsync(() -> gateway.deleteById(id));
	}
}
