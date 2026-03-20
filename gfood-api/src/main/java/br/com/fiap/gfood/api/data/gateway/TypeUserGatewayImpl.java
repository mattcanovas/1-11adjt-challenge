package br.com.fiap.gfood.api.data.gateway;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import br.com.fiap.gfood.api.data.entity.TypeUserEntity;
import br.com.fiap.gfood.api.data.repository.TypeUserRepository;
import br.com.fiap.gfood.api.domain.gateway.TypeUserGateway;
import br.com.fiap.gfood.api.domain.model.TypeUser;

@Component
public class TypeUserGatewayImpl implements TypeUserGateway
{
	private final TypeUserRepository repository;

	public TypeUserGatewayImpl(TypeUserRepository repository)
	{
		this.repository = repository;
	}

	@Override
	public Page<TypeUser> findAll(Pageable pageable)
	{
		return repository.findAll(pageable).map(this::toDomain);
	}

	@Override
	public Page<TypeUser> findByNameContaining(String name, Pageable pageable)
	{
		return repository.findAllByNameContaining(name, pageable).map(this::toDomain);
	}

	@Override
	public TypeUser save(TypeUser typeUser)
	{
		TypeUserEntity entity = toEntity(typeUser);
		return toDomain(repository.save(entity));
	}

	@Override
	public Optional<TypeUser> findById(UUID id)
	{
		return repository.findById(id).map(this::toDomain);
	}

	@Override
	public void deleteById(UUID id)
	{
		repository.deleteById(id);
	}

	@Override
	public boolean existsByName(String name)
	{
		return repository.existsByName(name);
	}

	private TypeUser toDomain(TypeUserEntity entity)
	{
		return TypeUser.builder()
				.id(entity.getId())
				.name(entity.getName())
				.description(entity.getDescription())
				.createdAt(entity.getCreatedAt())
				.updatedAt(entity.getUpdatedAt())
				.build();
	}

	private TypeUserEntity toEntity(TypeUser typeUser)
	{
		return TypeUserEntity.builder()
				.id(typeUser.getId())
				.name(typeUser.getName())
				.description(typeUser.getDescription())
				.createdAt(typeUser.getCreatedAt())
				.updatedAt(typeUser.getUpdatedAt())
				.build();
	}
}
