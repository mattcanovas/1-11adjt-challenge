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

import br.com.fiap.gfood.api.application.dto.CreateUserRequest;
import br.com.fiap.gfood.api.domain.exception.EmailAlreadyUsedException;
import br.com.fiap.gfood.api.domain.exception.TypeUserNotFoundException;
import br.com.fiap.gfood.api.domain.gateway.TypeUserGateway;
import br.com.fiap.gfood.api.domain.gateway.UserGateway;
import br.com.fiap.gfood.api.domain.model.TypeUser;
import br.com.fiap.gfood.api.domain.model.User;

@ExtendWith(MockitoExtension.class)
class CreateUserUseCaseTest
{
	@Mock
	private UserGateway userGateway;

	@Mock
	private TypeUserGateway typeUserGateway;

	@InjectMocks
	private CreateUserUseCase useCase;

	@Test
	void shouldCreateUserSuccessfully()
	{
		UUID typeUserId = UUID.randomUUID();
		TypeUser typeUser = TypeUser.builder().id(typeUserId).name("DEFAULT").build();
		CreateUserRequest request = new CreateUserRequest("John Doe", "john@test.com", "john.doe", "pass123", typeUserId, "Rua A 123");
		User savedUser = User.builder().fullName("John Doe").email("john@test.com").typeUser(typeUser).build();

		when(userGateway.existsByEmail("john@test.com")).thenReturn(false);
		when(typeUserGateway.findById(typeUserId)).thenReturn(Optional.of(typeUser));
		when(userGateway.save(any(User.class))).thenReturn(savedUser);

		User result = useCase.execute(request);

		assertNotNull(result);
		assertEquals("John Doe", result.getFullName());
		assertNotNull(result.getTypeUser());
		verify(userGateway).existsByEmail("john@test.com");
		verify(typeUserGateway).findById(typeUserId);
		verify(userGateway).save(any(User.class));
	}

	@Test
	void shouldThrowExceptionWhenEmailAlreadyExists()
	{
		UUID typeUserId = UUID.randomUUID();
		CreateUserRequest request = new CreateUserRequest("John Doe", "john@test.com", "john.doe", "pass123", typeUserId, "Rua A 123");

		when(userGateway.existsByEmail("john@test.com")).thenReturn(true);

		assertThrows(EmailAlreadyUsedException.class, () -> useCase.execute(request));
		verify(userGateway).existsByEmail("john@test.com");
		verify(userGateway, never()).save(any(User.class));
	}

	@Test
	void shouldThrowExceptionWhenTypeUserNotFound()
	{
		UUID typeUserId = UUID.randomUUID();
		CreateUserRequest request = new CreateUserRequest("John Doe", "john@test.com", "john.doe", "pass123", typeUserId, "Rua A 123");

		when(userGateway.existsByEmail("john@test.com")).thenReturn(false);
		when(typeUserGateway.findById(typeUserId)).thenReturn(Optional.empty());

		assertThrows(TypeUserNotFoundException.class, () -> useCase.execute(request));
		verify(typeUserGateway).findById(typeUserId);
		verify(userGateway, never()).save(any(User.class));
	}
}
