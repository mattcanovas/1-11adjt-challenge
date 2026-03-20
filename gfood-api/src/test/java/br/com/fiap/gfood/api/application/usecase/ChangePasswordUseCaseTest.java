package br.com.fiap.gfood.api.application.usecase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.fiap.gfood.api.application.dto.ChangePasswordRequest;
import br.com.fiap.gfood.api.domain.exception.PasswordConfirmationMismatchException;
import br.com.fiap.gfood.api.domain.exception.PasswordMismatchException;
import br.com.fiap.gfood.api.domain.exception.UserNotFoundException;
import br.com.fiap.gfood.api.domain.gateway.UserGateway;
import br.com.fiap.gfood.api.domain.model.User;

@ExtendWith(MockitoExtension.class)
class ChangePasswordUseCaseTest
{
	@Mock
	private UserGateway gateway;

	@InjectMocks
	private ChangePasswordUseCase useCase;

	@Test
	void shouldChangePasswordSuccessfully()
	{
		UUID userId = UUID.randomUUID();
		User user = User.builder().id(userId).fullName("John Doe").password("oldPass").build();
		User updatedUser = User.builder().id(userId).fullName("John Doe").password("newPass").build();
		ChangePasswordRequest request = new ChangePasswordRequest("oldPass", "newPass", "newPass");

		when(gateway.findById(userId)).thenReturn(Optional.of(user));
		when(gateway.save(any(User.class))).thenReturn(updatedUser);

		User result = useCase.execute(userId, request);

		assertNotNull(result);
		assertEquals("newPass", result.getPassword());
		verify(gateway).findById(userId);
		verify(gateway).save(any(User.class));
	}

	@Test
	void shouldThrowExceptionWhenUserNotFound()
	{
		UUID userId = UUID.randomUUID();
		ChangePasswordRequest request = new ChangePasswordRequest("oldPass", "newPass", "newPass");

		when(gateway.findById(userId)).thenReturn(Optional.empty());

		assertThrows(UserNotFoundException.class, () -> useCase.execute(userId, request));
		verify(gateway).findById(userId);
		verify(gateway, never()).save(any(User.class));
	}

	@Test
	void shouldThrowExceptionWhenOldPasswordMismatch()
	{
		UUID userId = UUID.randomUUID();
		User user = User.builder().id(userId).password("actualOldPass").build();
		ChangePasswordRequest request = new ChangePasswordRequest("wrongOldPass", "newPass", "newPass");

		when(gateway.findById(userId)).thenReturn(Optional.of(user));

		assertThrows(PasswordMismatchException.class, () -> useCase.execute(userId, request));
		verify(gateway).findById(userId);
		verify(gateway, never()).save(any(User.class));
	}

	@Test
	void shouldThrowExceptionWhenConfirmationPasswordMismatch()
	{
		UUID userId = UUID.randomUUID();
		User user = User.builder().id(userId).password("oldPass").build();
		ChangePasswordRequest request = new ChangePasswordRequest("oldPass", "newPass", "differentPass");

		when(gateway.findById(userId)).thenReturn(Optional.of(user));

		assertThrows(PasswordConfirmationMismatchException.class, () -> useCase.execute(userId, request));
		verify(gateway).findById(userId);
		verify(gateway, never()).save(any(User.class));
	}
}
