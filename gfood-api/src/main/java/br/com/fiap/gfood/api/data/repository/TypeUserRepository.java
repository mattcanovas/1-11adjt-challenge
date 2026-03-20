package br.com.fiap.gfood.api.data.repository;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import br.com.fiap.gfood.api.data.entity.TypeUserEntity;

public interface TypeUserRepository extends JpaRepository<TypeUserEntity, UUID>
{
	Page<TypeUserEntity> findAllByNameContaining(String name, Pageable pageable);

	boolean existsByName(String name);
}
