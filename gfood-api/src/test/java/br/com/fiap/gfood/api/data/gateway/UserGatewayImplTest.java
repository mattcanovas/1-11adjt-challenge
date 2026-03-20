package br.com.fiap.gfood.api.data.gateway;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
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
import br.com.fiap.gfood.api.data.entity.UserEntity;
import br.com.fiap.gfood.api.data.repository.UserRepository;
import br.com.fiap.gfood.api.domain.model.TypeUser;
import br.com.fiap.gfood.api.domain.model.User;

@ExtendWith(MockitoExtension.class)
class UserGatewayImplTest
{
	@Mock
	private UserRepository repository;

	@InjectMocks
	private UserGatewayImpl gateway;

	private final LocalDateTime now = LocalDateTime.now();

	private TypeUserEntity buildTypeUserEntity()
	{
		return TypeUserEntity.builder()
				.id(UUID.randomUUID()).name("DEFAULT").description("Default user")
				.createdAt(now).updatedAt(now).build();
	}

	private UserEntity buildUserEntity(TypeUserEntity typeUserEntity)
	{
		return UserEntity.builder()
				.id(UUID.randomUUID()).fullName("John Doe").login("john.doe")
				.password("pass123").email("john@test.com").address("Rua A 123")
				.typeUser(typeUserEntity).createdAt(now).updatedAt(now).build();
	}

	@Test
	void shouldFindAllWithPagination()
	{
		Pageable pageable = PageRequest.of(0, 10);
		TypeUserEntity typeUserEntity = buildTypeUserEntity();
		UserEntity entity = buildUserEntity(typeUserEntity);
		Page<UserEntity> page = new PageImpl<>(List.of(entity));

		when(repository.findAll(pageable)).thenReturn(page);

		Page<User> result = gateway.findAll(pageable);

		assertNotNull(result);
		assertEquals(1, result.getTotalElements());
		assertEquals("John Doe", result.getContent().get(0).getFullName());
		assertNotNull(result.getContent().get(0).getTypeUser());
		verify(repository).findAll(pageable);
	}

	@Test
	void shouldFindByFullNameContaining()
	{
		Pageable pageable = PageRequest.of(0, 10);
		TypeUserEntity typeUserEntity = buildTypeUserEntity();
		UserEntity entity = buildUserEntity(typeUserEntity);
		Page<UserEntity> page = new PageImpl<>(List.of(entity));

		when(repository.findAllByFullNameContaining("John", pageable)).thenReturn(page);

		Page<User> result = gateway.findByFullNameContaining("John", pageable);

		assertNotNull(result);
		assertEquals(1, result.getTotalElements());
		assertEquals("John Doe", result.getContent().get(0).getFullName());
		verify(repository).findAllByFullNameContaining("John", pageable);
	}

	@Test
	void shouldSaveAndReturnDomainModel()
	{
		TypeUser typeUser = TypeUser.builder()
				.id(UUID.randomUUID()).name("DEFAULT").description("Default user")
				.createdAt(now).updatedAt(now).build();
		User user = User.builder()
				.fullName("John Doe").login("john.doe").password("pass123")
				.email("john@test.com").address("Rua A 123").typeUser(typeUser).build();

		TypeUserEntity typeUserEntity = buildTypeUserEntity();
		UserEntity savedEntity = buildUserEntity(typeUserEntity);

		when(repository.save(any(UserEntity.class))).thenReturn(savedEntity);

		User result = gateway.save(user);

		assertNotNull(result);
		assertNotNull(result.getId());
		assertEquals("John Doe", result.getFullName());
		assertEquals("john.doe", result.getLogin());
		assertEquals("john@test.com", result.getEmail());
		assertNotNull(result.getTypeUser());
		verify(repository).save(any(UserEntity.class));
	}

	@Test
	void shouldSaveUserWithNullTypeUser()
	{
		User user = User.builder()
				.fullName("John Doe").login("john.doe").password("pass123")
				.email("john@test.com").address("Rua A 123").typeUser(null).build();

		UserEntity savedEntity = UserEntity.builder()
				.id(UUID.randomUUID()).fullName("John Doe").login("john.doe")
				.password("pass123").email("john@test.com").address("Rua A 123")
				.typeUser(null).createdAt(now).updatedAt(now).build();

		when(repository.save(any(UserEntity.class))).thenReturn(savedEntity);

		User result = gateway.save(user);

		assertNotNull(result);
		assertNull(result.getTypeUser());
		verify(repository).save(any(UserEntity.class));
	}

	@Test
	void shouldFindById()
	{
		UUID id = UUID.randomUUID();
		TypeUserEntity typeUserEntity = buildTypeUserEntity();
		UserEntity entity = buildUserEntity(typeUserEntity);

		when(repository.findById(id)).thenReturn(Optional.of(entity));

		Optional<User> result = gateway.findById(id);

		assertTrue(result.isPresent());
		assertEquals("John Doe", result.get().getFullName());
		verify(repository).findById(id);
	}

	@Test
	void shouldReturnEmptyWhenNotFoundById()
	{
		UUID id = UUID.randomUUID();

		when(repository.findById(id)).thenReturn(Optional.empty());

		Optional<User> result = gateway.findById(id);

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
	void shouldReturnTrueWhenEmailExists()
	{
		when(repository.existsByEmail("john@test.com")).thenReturn(true);

		assertTrue(gateway.existsByEmail("john@test.com"));
		verify(repository).existsByEmail("john@test.com");
	}

	@Test
	void shouldReturnFalseWhenEmailDoesNotExist()
	{
		when(repository.existsByEmail("unknown@test.com")).thenReturn(false);

		assertFalse(gateway.existsByEmail("unknown@test.com"));
		verify(repository).existsByEmail("unknown@test.com");
	}

	@Test
	void shouldFindByLoginAndPassword()
	{
		TypeUserEntity typeUserEntity = buildTypeUserEntity();
		UserEntity entity = buildUserEntity(typeUserEntity);

		when(repository.findByLoginAndPassword("john.doe", "pass123")).thenReturn(Optional.of(entity));

		Optional<User> result = gateway.findByLoginAndPassword("john.doe", "pass123");

		assertTrue(result.isPresent());
		assertEquals("John Doe", result.get().getFullName());
		verify(repository).findByLoginAndPassword("john.doe", "pass123");
	}

	@Test
	void shouldReturnEmptyWhenLoginAndPasswordNotFound()
	{
		when(repository.findByLoginAndPassword("wrong", "wrong")).thenReturn(Optional.empty());

		Optional<User> result = gateway.findByLoginAndPassword("wrong", "wrong");

		assertFalse(result.isPresent());
		verify(repository).findByLoginAndPassword("wrong", "wrong");
	}
}
