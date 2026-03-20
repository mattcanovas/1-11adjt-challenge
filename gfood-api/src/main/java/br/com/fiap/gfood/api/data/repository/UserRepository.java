package br.com.fiap.gfood.api.data.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import br.com.fiap.gfood.api.data.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, UUID>
{
	Page<UserEntity> findAllByFullNameContaining(String firstName, Pageable pageable);

	boolean existsByEmail(String email);

	Optional<UserEntity> findByLoginAndPassword(String login, String password);
}
