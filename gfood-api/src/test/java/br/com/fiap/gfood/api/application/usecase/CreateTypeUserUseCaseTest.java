package br.com.fiap.gfood.api.application.usecase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.fiap.gfood.api.application.dto.CreateTypeUserRequest;
import br.com.fiap.gfood.api.domain.exception.TypeUserNameAlreadyExistsException;
import br.com.fiap.gfood.api.domain.gateway.TypeUserGateway;
import br.com.fiap.gfood.api.domain.model.TypeUser;

@ExtendWith(MockitoExtension.class)
class CreateTypeUserUseCaseTest
{
	@Mock
	private TypeUserGateway gateway;

	@InjectMocks
	private CreateTypeUserUseCase useCase;

	@Test
	void shouldCreateTypeUserSuccessfully()
	{
		CreateTypeUserRequest request = new CreateTypeUserRequest("ADMIN", "Administrator users");
		TypeUser saved = TypeUser.builder().name("ADMIN").description("Administrator users").build();

		when(gateway.existsByName("ADMIN")).thenReturn(false);
		when(gateway.save(any(TypeUser.class))).thenReturn(saved);

		TypeUser result = useCase.execute(request);

		assertNotNull(result);
		assertEquals("ADMIN", result.getName());
		assertEquals("Administrator users", result.getDescription());
		verify(gateway).existsByName("ADMIN");
		verify(gateway).save(any(TypeUser.class));
	}

	@Test
	void shouldThrowExceptionWhenNameAlreadyExists()
	{
		CreateTypeUserRequest request = new CreateTypeUserRequest("ADMIN", "Administrator users");

		when(gateway.existsByName("ADMIN")).thenReturn(true);

		assertThrows(TypeUserNameAlreadyExistsException.class, () -> useCase.execute(request));
		verify(gateway).existsByName("ADMIN");
		verify(gateway, never()).save(any(TypeUser.class));
	}
}
