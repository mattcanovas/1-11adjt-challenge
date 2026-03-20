package br.com.fiap.gfood.api.domain.gateway;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.com.fiap.gfood.api.domain.model.TypeUser;

public interface TypeUserGateway
{
	Page<TypeUser> findAll(Pageable pageable);

	Page<TypeUser> findByNameContaining(String name, Pageable pageable);

	TypeUser save(TypeUser typeUser);

	Optional<TypeUser> findById(UUID id);

	void deleteById(UUID id);

	boolean existsByName(String name);
}
