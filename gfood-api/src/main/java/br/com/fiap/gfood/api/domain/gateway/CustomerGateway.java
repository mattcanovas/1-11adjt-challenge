package br.com.fiap.gfood.api.domain.gateway;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.com.fiap.gfood.api.domain.model.Customer;

public interface CustomerGateway
{
	Page<Customer> findAll(Pageable pageable);

	Page<Customer> findByFullNameContaining(String fullName, Pageable pageable);

	Customer save(Customer customer);

	Optional<Customer> findById(UUID id);

	void deleteById(UUID id);

	boolean existsByEmail(String email);

	Optional<Customer> findByLoginAndPassword(String login, String password);
}
