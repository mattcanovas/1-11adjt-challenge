package br.com.fiap.gfood.api.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.fiap.gfood.api.entities.Customer;

public interface CustomerRepository extends JpaRepository<Customer, UUID>
{

}
