package br.com.fiap.gfood.api.application.usecase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import br.com.fiap.gfood.api.domain.exception.TypeUserNotFoundException;
import br.com.fiap.gfood.api.domain.gateway.TypeUserGateway;
import br.com.fiap.gfood.api.domain.model.TypeUser;

@ExtendWith(MockitoExtension.class)
class FindTypeUsersUseCaseTest
{
	@Mock
	private TypeUserGateway gateway;

	@InjectMocks
	private FindTypeUsersUseCase useCase;

	@Test
	void shouldFindAllTypeUsersWhenNameIsNull()
	{
		Pageable pageable = PageRequest.of(0, 10);
		TypeUser typeUser = TypeUser.builder().name("DEFAULT").description("Default user").build();
		Page<TypeUser> page = new PageImpl<>(List.of(typeUser));

		when(gateway.findAll(pageable)).thenReturn(page);

		Page<TypeUser> result = useCase.execute(null, pageable);

		assertNotNull(result);
		assertEquals(1, result.getTotalElements());
		verify(gateway).findAll(pageable);
	}

	@Test
	void shouldFindAllTypeUsersWhenNameIsBlank()
	{
		Pageable pageable = PageRequest.of(0, 10);
		Page<TypeUser> page = new PageImpl<>(List.of());

		when(gateway.findAll(pageable)).thenReturn(page);

		Page<TypeUser> result = useCase.execute("", pageable);

		assertNotNull(result);
		verify(gateway).findAll(pageable);
	}

	@Test
	void shouldFindTypeUsersByNameContaining()
	{
		Pageable pageable = PageRequest.of(0, 10);
		TypeUser typeUser = TypeUser.builder().name("ADMIN").description("Admin").build();
		Page<TypeUser> page = new PageImpl<>(List.of(typeUser));

		when(gateway.findByNameContaining("ADM", pageable)).thenReturn(page);

		Page<TypeUser> result = useCase.execute("ADM", pageable);

		assertNotNull(result);
		assertEquals(1, result.getTotalElements());
		assertEquals("ADMIN", result.getContent().get(0).getName());
		verify(gateway).findByNameContaining("ADM", pageable);
	}

	@Test
	void shouldFindTypeUserById()
	{
		UUID id = UUID.randomUUID();
		TypeUser typeUser = TypeUser.builder().id(id).name("DEFAULT").build();

		when(gateway.findById(id)).thenReturn(Optional.of(typeUser));

		TypeUser result = useCase.executeById(id);

		assertNotNull(result);
		assertEquals(id, result.getId());
		assertEquals("DEFAULT", result.getName());
		verify(gateway).findById(id);
	}

	@Test
	void shouldThrowExceptionWhenTypeUserNotFoundById()
	{
		UUID id = UUID.randomUUID();

		when(gateway.findById(id)).thenReturn(Optional.empty());

		assertThrows(TypeUserNotFoundException.class, () -> useCase.executeById(id));
		verify(gateway).findById(id);
	}
}
