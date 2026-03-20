package br.com.fiap.gfood.api.data.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import br.com.fiap.gfood.api.data.entity.CustomerEntity;

public interface CustomerRepository extends JpaRepository<CustomerEntity, UUID>
{
	Page<CustomerEntity> findAllByFullNameContaining(String firstName, Pageable pageable);

	boolean existsByEmail(String email);

	Optional<CustomerEntity> findByLoginAndPassword(String login, String password);
}
