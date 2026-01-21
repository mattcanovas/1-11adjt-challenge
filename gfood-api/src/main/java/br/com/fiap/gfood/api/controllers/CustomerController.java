package br.com.fiap.gfood.api.controllers;

import static br.com.fiap.gfood.api.utils.Constants.CREATED_AT_SORTING_PARAM;
import static br.com.fiap.gfood.api.utils.Constants.V1_CUSTOMER;
import static org.springframework.http.HttpStatus.ACCEPTED;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.fiap.gfood.api.entities.Customer;
import br.com.fiap.gfood.api.models.CreateCustomerRequest;
import br.com.fiap.gfood.api.service.CustomerService;

@RestController
@RequestMapping(value = V1_CUSTOMER)
public class CustomerController
{
	private final CustomerService service;

	public CustomerController(final CustomerService service)
	{
		this.service = service;
	}

	@GetMapping
	public ResponseEntity<Object> pageAll(final Integer page, final Integer size)
	{
		Page<Customer> allCustomers = this.service
				.pageAll(PageRequest.of(page, size, Sort.by(CREATED_AT_SORTING_PARAM)));
		return new ResponseEntity<>(allCustomers, HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Object> findById(@PathVariable
	final UUID id)
	{
		Customer customer = this.service.findById(id);
		return new ResponseEntity<>(customer, OK);
	}

	@PostMapping
	public ResponseEntity<Object> create(@RequestBody @Validated CreateCustomerRequest requestBody)
	{
		Customer customerCreated = service.create(requestBody);
		return new ResponseEntity<>(customerCreated, CREATED);
	}
	
	@PostMapping
	public ResponseEntity<Object> signIn() {
		return new ResponseEntity<>(OK);
	}

	@PutMapping
	public ResponseEntity<Object> update()
	{
		return new ResponseEntity<>(OK);
	}

	@PatchMapping
	public ResponseEntity<Object> changePassword()
	{
		return new ResponseEntity<>(OK);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Object> delete(@PathVariable UUID id)
	{
		service.deleteById(id);
		return new ResponseEntity<>(ACCEPTED);
	}
}
