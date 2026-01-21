package br.com.fiap.gfood.api.service;

import static br.com.fiap.gfood.api.utils.Constants.MESSAGE_ERROR_CUSTOMER_NOT_FOUND;
import static java.util.concurrent.CompletableFuture.runAsync;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.fiap.gfood.api.entities.Customer;
import br.com.fiap.gfood.api.models.CreateCustomerRequest;
import br.com.fiap.gfood.api.repositories.CustomerRepository;
import jakarta.persistence.EntityNotFoundException;

@Service
public class CustomerServiceImpl implements CustomerService
{
	private final CustomerRepository repository;

	public CustomerServiceImpl(final CustomerRepository repository)
	{
		this.repository = repository;
	}

	@Override
	public Page<Customer> pageAll(Pageable pageable)
	{
		return repository.findAll(pageable);
	}

	@Override
	public Customer findById(UUID id)
	{
		return repository.findById(id).orElseThrow(() -> new EntityNotFoundException(MESSAGE_ERROR_CUSTOMER_NOT_FOUND));
	}

	@Override
	public Customer create(CreateCustomerRequest requestBody)
	{
		return null;
	}

	@Override
	public void deleteById(UUID id)
	{
		runAsync(() -> {
			repository.deleteById(id);
		});
	}

}
