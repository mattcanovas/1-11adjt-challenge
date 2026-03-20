package br.com.fiap.gfood.api.application.usecase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
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

import br.com.fiap.gfood.api.domain.gateway.UserGateway;
import br.com.fiap.gfood.api.domain.model.User;

@ExtendWith(MockitoExtension.class)
class FindUsersUseCaseTest
{
	@Mock
	private UserGateway gateway;

	@InjectMocks
	private FindUsersUseCase useCase;

	@Test
	void shouldFindAllUsersWhenFullNameIsNull()
	{
		Pageable pageable = PageRequest.of(0, 10);
		User user = User.builder().id(UUID.randomUUID()).fullName("John Doe").build();
		Page<User> page = new PageImpl<>(List.of(user));

		when(gateway.findAll(pageable)).thenReturn(page);

		Page<User> result = useCase.execute(null, pageable);

		assertNotNull(result);
		assertEquals(1, result.getTotalElements());
		assertEquals("John Doe", result.getContent().get(0).getFullName());
		verify(gateway).findAll(pageable);
		verify(gateway, never()).findByFullNameContaining(null, pageable);
	}

	@Test
	void shouldFindAllUsersWhenFullNameIsBlank()
	{
		Pageable pageable = PageRequest.of(0, 10);
		User user = User.builder().id(UUID.randomUUID()).fullName("Jane Doe").build();
		Page<User> page = new PageImpl<>(List.of(user));

		when(gateway.findAll(pageable)).thenReturn(page);

		Page<User> result = useCase.execute("  ", pageable);

		assertNotNull(result);
		assertEquals(1, result.getTotalElements());
		verify(gateway).findAll(pageable);
	}

	@Test
	void shouldFindUsersByFullNameWhenProvided()
	{
		Pageable pageable = PageRequest.of(0, 10);
		User user = User.builder().id(UUID.randomUUID()).fullName("John Doe").build();
		Page<User> page = new PageImpl<>(List.of(user));

		when(gateway.findByFullNameContaining("John", pageable)).thenReturn(page);

		Page<User> result = useCase.execute("John", pageable);

		assertNotNull(result);
		assertEquals(1, result.getTotalElements());
		assertEquals("John Doe", result.getContent().get(0).getFullName());
		verify(gateway).findByFullNameContaining("John", pageable);
		verify(gateway, never()).findAll(pageable);
	}
}
