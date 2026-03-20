package br.com.fiap.gfood.api.application.usecase;

import static java.util.concurrent.CompletableFuture.runAsync;

import java.util.UUID;

import org.springframework.stereotype.Service;

import br.com.fiap.gfood.api.domain.gateway.CustomerGateway;

@Service
public class DeleteCustomerUseCase
{
	private final CustomerGateway gateway;

	public DeleteCustomerUseCase(CustomerGateway gateway)
	{
		this.gateway = gateway;
	}

	public void execute(UUID id)
	{
		runAsync(() -> gateway.deleteById(id));
	}
}
