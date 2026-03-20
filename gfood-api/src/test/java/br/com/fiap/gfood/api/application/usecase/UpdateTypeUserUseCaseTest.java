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

import br.com.fiap.gfood.api.application.dto.UpdateTypeUserRequest;
import br.com.fiap.gfood.api.domain.exception.TypeUserNameAlreadyExistsException;
import br.com.fiap.gfood.api.domain.exception.TypeUserNotFoundException;
import br.com.fiap.gfood.api.domain.gateway.TypeUserGateway;
import br.com.fiap.gfood.api.domain.model.TypeUser;

@ExtendWith(MockitoExtension.class)
class UpdateTypeUserUseCaseTest
{
	@Mock
	private TypeUserGateway gateway;

	@InjectMocks
	private UpdateTypeUserUseCase useCase;

	@Test
	void shouldUpdateTypeUserSuccessfully()
	{
		UUID id = UUID.randomUUID();
		TypeUser existing = TypeUser.builder().id(id).name("DEFAULT").description("Default").build();
		UpdateTypeUserRequest request = new UpdateTypeUserRequest("PREMIUM", "Premium users");

		when(gateway.findById(id)).thenReturn(Optional.of(existing));
		when(gateway.existsByName("PREMIUM")).thenReturn(false);
		when(gateway.save(any(TypeUser.class))).thenAnswer(invocation -> invocation.getArgument(0));

		TypeUser result = useCase.execute(id, request);

		assertNotNull(result);
		assertEquals("PREMIUM", result.getName());
		assertEquals("Premium users", result.getDescription());
		verify(gateway).findById(id);
		verify(gateway).existsByName("PREMIUM");
		verify(gateway).save(any(TypeUser.class));
	}

	@Test
	void shouldThrowExceptionWhenTypeUserNotFound()
	{
		UUID id = UUID.randomUUID();
		UpdateTypeUserRequest request = new UpdateTypeUserRequest("PREMIUM", "Premium users");

		when(gateway.findById(id)).thenReturn(Optional.empty());

		assertThrows(TypeUserNotFoundException.class, () -> useCase.execute(id, request));
		verify(gateway).findById(id);
		verify(gateway, never()).save(any(TypeUser.class));
	}

	@Test
	void shouldThrowExceptionWhenNameAlreadyExists()
	{
		UUID id = UUID.randomUUID();
		TypeUser existing = TypeUser.builder().id(id).name("DEFAULT").description("Default").build();
		UpdateTypeUserRequest request = new UpdateTypeUserRequest("ADMIN", "Admin users");

		when(gateway.findById(id)).thenReturn(Optional.of(existing));
		when(gateway.existsByName("ADMIN")).thenReturn(true);

		assertThrows(TypeUserNameAlreadyExistsException.class, () -> useCase.execute(id, request));
		verify(gateway).findById(id);
		verify(gateway).existsByName("ADMIN");
		verify(gateway, never()).save(any(TypeUser.class));
	}

	@Test
	void shouldSkipNameCheckWhenNameUnchanged()
	{
		UUID id = UUID.randomUUID();
		TypeUser existing = TypeUser.builder().id(id).name("DEFAULT").description("Default").build();
		UpdateTypeUserRequest request = new UpdateTypeUserRequest("DEFAULT", "Updated description");

		when(gateway.findById(id)).thenReturn(Optional.of(existing));
		when(gateway.save(any(TypeUser.class))).thenAnswer(invocation -> invocation.getArgument(0));

		TypeUser result = useCase.execute(id, request);

		assertNotNull(result);
		assertEquals("DEFAULT", result.getName());
		assertEquals("Updated description", result.getDescription());
		verify(gateway).findById(id);
		verify(gateway, never()).existsByName(any());
		verify(gateway).save(any(TypeUser.class));
	}
}
