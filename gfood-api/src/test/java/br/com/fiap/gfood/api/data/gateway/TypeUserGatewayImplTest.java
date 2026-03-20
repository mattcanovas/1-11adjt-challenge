package br.com.fiap.gfood.api.data.gateway;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
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

import br.com.fiap.gfood.api.data.entity.TypeUserEntity;
import br.com.fiap.gfood.api.data.repository.TypeUserRepository;
import br.com.fiap.gfood.api.domain.model.TypeUser;

@ExtendWith(MockitoExtension.class)
class TypeUserGatewayImplTest
{
	@Mock
	private TypeUserRepository repository;

	@InjectMocks
	private TypeUserGatewayImpl gateway;

	@Test
	void shouldSaveAndReturnDomainModel()
	{
		UUID id = UUID.randomUUID();
		LocalDateTime now = LocalDateTime.now();
		TypeUser domain = TypeUser.builder().name("DEFAULT").description("Default user").build();
		TypeUserEntity savedEntity = TypeUserEntity.builder()
				.id(id).name("DEFAULT").description("Default user")
				.createdAt(now).updatedAt(now).build();

		when(repository.save(any(TypeUserEntity.class))).thenReturn(savedEntity);

		TypeUser result = gateway.save(domain);

		assertNotNull(result);
		assertEquals(id, result.getId());
		assertEquals("DEFAULT", result.getName());
		assertEquals("Default user", result.getDescription());
		verify(repository).save(any(TypeUserEntity.class));
	}

	@Test
	void shouldFindAllWithPagination()
	{
		Pageable pageable = PageRequest.of(0, 10);
		UUID id = UUID.randomUUID();
		LocalDateTime now = LocalDateTime.now();
		TypeUserEntity entity = TypeUserEntity.builder()
				.id(id).name("ADMIN").description("Admin")
				.createdAt(now).updatedAt(now).build();
		Page<TypeUserEntity> page = new PageImpl<>(List.of(entity));

		when(repository.findAll(pageable)).thenReturn(page);

		Page<TypeUser> result = gateway.findAll(pageable);

		assertNotNull(result);
		assertEquals(1, result.getTotalElements());
		assertEquals("ADMIN", result.getContent().get(0).getName());
		verify(repository).findAll(pageable);
	}

	@Test
	void shouldFindByNameContaining()
	{
		Pageable pageable = PageRequest.of(0, 10);
		UUID id = UUID.randomUUID();
		LocalDateTime now = LocalDateTime.now();
		TypeUserEntity entity = TypeUserEntity.builder()
				.id(id).name("ADMIN").description("Admin")
				.createdAt(now).updatedAt(now).build();
		Page<TypeUserEntity> page = new PageImpl<>(List.of(entity));

		when(repository.findAllByNameContaining("ADM", pageable)).thenReturn(page);

		Page<TypeUser> result = gateway.findByNameContaining("ADM", pageable);

		assertNotNull(result);
		assertEquals(1, result.getTotalElements());
		verify(repository).findAllByNameContaining("ADM", pageable);
	}

	@Test
	void shouldFindById()
	{
		UUID id = UUID.randomUUID();
		LocalDateTime now = LocalDateTime.now();
		TypeUserEntity entity = TypeUserEntity.builder()
				.id(id).name("OWNER").description("Owner")
				.createdAt(now).updatedAt(now).build();

		when(repository.findById(id)).thenReturn(Optional.of(entity));

		Optional<TypeUser> result = gateway.findById(id);

		assertTrue(result.isPresent());
		assertEquals("OWNER", result.get().getName());
		verify(repository).findById(id);
	}

	@Test
	void shouldReturnEmptyWhenNotFoundById()
	{
		UUID id = UUID.randomUUID();

		when(repository.findById(id)).thenReturn(Optional.empty());

		Optional<TypeUser> result = gateway.findById(id);

		assertFalse(result.isPresent());
		verify(repository).findById(id);
	}

	@Test
	void shouldDeleteById()
	{
		UUID id = UUID.randomUUID();

		gateway.deleteById(id);

		verify(repository).deleteById(id);
	}

	@Test
	void shouldReturnTrueWhenNameExists()
	{
		when(repository.existsByName("ADMIN")).thenReturn(true);

		assertTrue(gateway.existsByName("ADMIN"));
		verify(repository).existsByName("ADMIN");
	}

	@Test
	void shouldReturnFalseWhenNameDoesNotExist()
	{
		when(repository.existsByName("UNKNOWN")).thenReturn(false);

		assertFalse(gateway.existsByName("UNKNOWN"));
		verify(repository).existsByName("UNKNOWN");
	}
}
