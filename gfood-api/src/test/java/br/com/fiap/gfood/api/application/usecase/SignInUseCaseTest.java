package br.com.fiap.gfood.api.application.usecase;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.fiap.gfood.api.application.dto.SignInRequest;
import br.com.fiap.gfood.api.domain.exception.AuthenticationFailedException;
import br.com.fiap.gfood.api.domain.gateway.UserGateway;
import br.com.fiap.gfood.api.domain.model.User;

@ExtendWith(MockitoExtension.class)
class SignInUseCaseTest
{
	@Mock
	private UserGateway gateway;

	@InjectMocks
	private SignInUseCase useCase;

	@Test
	void shouldSignInSuccessfully()
	{
		SignInRequest request = new SignInRequest("john.doe", "pass123");
		User user = User.builder().id(UUID.randomUUID()).login("john.doe").password("pass123").build();

		when(gateway.findByLoginAndPassword("john.doe", "pass123")).thenReturn(Optional.of(user));

		assertDoesNotThrow(() -> useCase.execute(request));
		verify(gateway).findByLoginAndPassword("john.doe", "pass123");
	}

	@Test
	void shouldThrowExceptionWhenCredentialsAreInvalid()
	{
		SignInRequest request = new SignInRequest("john.doe", "wrongPass");

		when(gateway.findByLoginAndPassword("john.doe", "wrongPass")).thenReturn(Optional.empty());

		assertThrows(AuthenticationFailedException.class, () -> useCase.execute(request));
		verify(gateway).findByLoginAndPassword("john.doe", "wrongPass");
	}
}
