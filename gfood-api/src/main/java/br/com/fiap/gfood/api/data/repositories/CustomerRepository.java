package br.com.fiap.gfood.api.data.repositories;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import br.com.fiap.gfood.api.data.entities.CustomerData;

public interface CustomerRepository extends JpaRepository<CustomerData, UUID>
{

	Page<CustomerData> findAllByFullNameContaining(String firstName, Pageable pageable);

	boolean existsByEmail(String email);

}
