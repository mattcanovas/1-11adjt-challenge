package br.com.fiap.gfood.api.application.usecase;

import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.fiap.gfood.api.domain.gateway.UserGateway;

@ExtendWith(MockitoExtension.class)
class DeleteUserUseCaseTest
{
	@Mock
	private UserGateway gateway;

	@InjectMocks
	private DeleteUserUseCase useCase;

	@Test
	void shouldDeleteUserById()
	{
		UUID userId = UUID.randomUUID();

		useCase.execute(userId);

		verify(gateway, timeout(1000)).deleteById(userId);
	}
}
