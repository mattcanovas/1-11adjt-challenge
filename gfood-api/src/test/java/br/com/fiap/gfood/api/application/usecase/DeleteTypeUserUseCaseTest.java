package br.com.fiap.gfood.api.application.usecase;

import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.fiap.gfood.api.domain.gateway.TypeUserGateway;

@ExtendWith(MockitoExtension.class)
class DeleteTypeUserUseCaseTest
{
	@Mock
	private TypeUserGateway gateway;

	@InjectMocks
	private DeleteTypeUserUseCase useCase;

	@Test
	void shouldDeleteTypeUserById()
	{
		UUID id = UUID.randomUUID();

		useCase.execute(id);

		verify(gateway, timeout(1000)).deleteById(id);
	}
}
