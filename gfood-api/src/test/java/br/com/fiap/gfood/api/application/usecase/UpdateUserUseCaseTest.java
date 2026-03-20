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

import br.com.fiap.gfood.api.application.dto.UpdateUserRequest;
import br.com.fiap.gfood.api.domain.exception.EmailAlreadyUsedException;
import br.com.fiap.gfood.api.domain.exception.TypeUserNotFoundException;
import br.com.fiap.gfood.api.domain.exception.UserNotFoundException;
import br.com.fiap.gfood.api.domain.gateway.TypeUserGateway;
import br.com.fiap.gfood.api.domain.gateway.UserGateway;
import br.com.fiap.gfood.api.domain.model.TypeUser;
import br.com.fiap.gfood.api.domain.model.User;

@ExtendWith(MockitoExtension.class)
class UpdateUserUseCaseTest
{
	@Mock
	private UserGateway userGateway;

	@Mock
	private TypeUserGateway typeUserGateway;

	@InjectMocks
	private UpdateUserUseCase useCase;

	@Test
	void shouldUpdateUserSuccessfully()
	{
		UUID userId = UUID.randomUUID();
		UUID typeUserId = UUID.randomUUID();
		TypeUser typeUser = TypeUser.builder().id(typeUserId).name("ADMIN").build();
		TypeUser existingType = TypeUser.builder().id(UUID.randomUUID()).name("DEFAULT").build();
		User existingUser = User.builder().id(userId).fullName("John Doe").email("john@test.com")
				.login("john.doe").typeUser(existingType).address("Rua A").build();
		UpdateUserRequest request = new UpdateUserRequest("John Updated", "john.updated@test.com", "john.updated", typeUserId, "Rua B 456");

		when(userGateway.findById(userId)).thenReturn(Optional.of(existingUser));
		when(userGateway.existsByEmail("john.updated@test.com")).thenReturn(false);
		when(typeUserGateway.findById(typeUserId)).thenReturn(Optional.of(typeUser));
		when(userGateway.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

		User result = useCase.execute(userId, request);

		assertNotNull(result);
		assertEquals("John Updated", result.getFullName());
		assertEquals("john.updated@test.com", result.getEmail());
		assertEquals("ADMIN", result.getTypeUser().getName());
		verify(userGateway).findById(userId);
		verify(typeUserGateway).findById(typeUserId);
		verify(userGateway).save(any(User.class));
	}

	@Test
	void shouldThrowExceptionWhenUserNotFound()
	{
		UUID userId = UUID.randomUUID();
		UUID typeUserId = UUID.randomUUID();
		UpdateUserRequest request = new UpdateUserRequest("John", "john@test.com", "john", typeUserId, "Rua A");

		when(userGateway.findById(userId)).thenReturn(Optional.empty());

		assertThrows(UserNotFoundException.class, () -> useCase.execute(userId, request));
		verify(userGateway, never()).save(any(User.class));
	}

	@Test
	void shouldThrowExceptionWhenEmailAlreadyUsed()
	{
		UUID userId = UUID.randomUUID();
		UUID typeUserId = UUID.randomUUID();
		TypeUser existingType = TypeUser.builder().id(UUID.randomUUID()).name("DEFAULT").build();
		User existingUser = User.builder().id(userId).fullName("John").email("old@test.com")
				.login("john").typeUser(existingType).address("Rua A").build();
		UpdateUserRequest request = new UpdateUserRequest("John", "taken@test.com", "john", typeUserId, "Rua A");

		when(userGateway.findById(userId)).thenReturn(Optional.of(existingUser));
		when(userGateway.existsByEmail("taken@test.com")).thenReturn(true);

		assertThrows(EmailAlreadyUsedException.class, () -> useCase.execute(userId, request));
		verify(userGateway, never()).save(any(User.class));
	}

	@Test
	void shouldThrowExceptionWhenTypeUserNotFound()
	{
		UUID userId = UUID.randomUUID();
		UUID typeUserId = UUID.randomUUID();
		TypeUser existingType = TypeUser.builder().id(UUID.randomUUID()).name("DEFAULT").build();
		User existingUser = User.builder().id(userId).fullName("John").email("john@test.com")
				.login("john").typeUser(existingType).address("Rua A").build();
		UpdateUserRequest request = new UpdateUserRequest("John", "new@test.com", "john", typeUserId, "Rua A");

		when(userGateway.findById(userId)).thenReturn(Optional.of(existingUser));
		when(userGateway.existsByEmail("new@test.com")).thenReturn(false);
		when(typeUserGateway.findById(typeUserId)).thenReturn(Optional.empty());

		assertThrows(TypeUserNotFoundException.class, () -> useCase.execute(userId, request));
		verify(userGateway, never()).save(any(User.class));
	}
}
