package br.com.fiap.gfood.api.service;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.com.fiap.gfood.api.entities.Customer;
import br.com.fiap.gfood.api.models.CreateCustomerRequest;

public interface CustomerService
{
	Page<Customer> pageAll(Pageable of);

	Customer findById(UUID id);

	Customer create(CreateCustomerRequest requestBody);

	void deleteById(UUID id);
}
