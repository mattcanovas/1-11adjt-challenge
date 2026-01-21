package br.com.fiap.gfood.api.presentation.customer;

import static br.com.fiap.gfood.api.core.commons.Constants.CREATED_AT_SORT_PARAMETER;
import static org.springframework.data.domain.PageRequest.of;
import static org.springframework.data.domain.Sort.by;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import br.com.fiap.gfood.api.core.domain.ApiPageResponse;
import br.com.fiap.gfood.api.core.domain.ApiResponse;
import br.com.fiap.gfood.api.core.services.customer.CustomerService;
import br.com.fiap.gfood.api.presentation.models.CreateCustomerRequest;
import br.com.fiap.gfood.api.presentation.models.UpdateCustomerRequest;

@Component
public class CustomerController implements CustomerResource
{
	private final CustomerService service;

	public CustomerController(final CustomerService gateway)
	{
		this.service = gateway;
	}

	@Override
	public ResponseEntity<ApiPageResponse> findAllFiltering(String fullName, Integer page, Integer size)
	{
		return ResponseEntity
				.ok(service.findAllFiltering(fullName, of(page, size, by(CREATED_AT_SORT_PARAMETER))));
	}

	@Override
	public ResponseEntity<ApiResponse> create(CreateCustomerRequest payload)
	{

		return new ResponseEntity<>(service.create(payload), HttpStatus.CREATED);
	}

	@Override
	public ResponseEntity<ApiResponse> update(UUID id, UpdateCustomerRequest payload)
	{
		return new ResponseEntity<>(service.update(id, payload), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<Object> delete(UUID id)
	{
		service.deleteById(id);
		return new ResponseEntity<>(HttpStatus.ACCEPTED);
	}

}
