package br.com.fiap.gfood.api.domain.gateway;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.com.fiap.gfood.api.domain.model.User;

public interface UserGateway
{
	Page<User> findAll(Pageable pageable);

	Page<User> findByFullNameContaining(String fullName, Pageable pageable);

	User save(User user);

	Optional<User> findById(UUID id);

	void deleteById(UUID id);

	boolean existsByEmail(String email);

	Optional<User> findByLoginAndPassword(String login, String password);
}
