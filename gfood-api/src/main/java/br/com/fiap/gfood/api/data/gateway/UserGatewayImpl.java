package br.com.fiap.gfood.api.data.gateway;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import br.com.fiap.gfood.api.data.entity.UserEntity;
import br.com.fiap.gfood.api.data.repository.UserRepository;
import br.com.fiap.gfood.api.domain.gateway.UserGateway;
import br.com.fiap.gfood.api.domain.model.User;

@Component
public class UserGatewayImpl implements UserGateway
{
	private final UserRepository repository;

	public UserGatewayImpl(UserRepository repository)
	{
		this.repository = repository;
	}

	@Override
	public Page<User> findAll(Pageable pageable)
	{
		return repository.findAll(pageable).map(this::toDomain);
	}

	@Override
	public Page<User> findByFullNameContaining(String fullName, Pageable pageable)
	{
		return repository.findAllByFullNameContaining(fullName, pageable).map(this::toDomain);
	}

	@Override
	public User save(User user)
	{
		UserEntity entity = toEntity(user);
		return toDomain(repository.save(entity));
	}

	@Override
	public Optional<User> findById(UUID id)
	{
		return repository.findById(id).map(this::toDomain);
	}

	@Override
	public void deleteById(UUID id)
	{
		repository.deleteById(id);
	}

	@Override
	public boolean existsByEmail(String email)
	{
		return repository.existsByEmail(email);
	}

	@Override
	public Optional<User> findByLoginAndPassword(String login, String password)
	{
		return repository.findByLoginAndPassword(login, password).map(this::toDomain);
	}

	private User toDomain(UserEntity entity)
	{
		return User.builder()
				.id(entity.getId())
				.fullName(entity.getFullName())
				.login(entity.getLogin())
				.password(entity.getPassword())
				.email(entity.getEmail())
				.type(entity.getType())
				.createdAt(entity.getCreatedAt())
				.updatedAt(entity.getUpdatedAt())
				.address(entity.getAddress())
				.build();
	}

	private UserEntity toEntity(User user)
	{
		return UserEntity.builder()
				.id(user.getId())
				.fullName(user.getFullName())
				.login(user.getLogin())
				.password(user.getPassword())
				.email(user.getEmail())
				.type(user.getType())
				.createdAt(user.getCreatedAt())
				.updatedAt(user.getUpdatedAt())
				.address(user.getAddress())
				.build();
	}
}
