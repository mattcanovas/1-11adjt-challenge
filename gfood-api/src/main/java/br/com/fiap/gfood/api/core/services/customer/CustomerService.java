package br.com.fiap.gfood.api.core.services.customer;

import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.fiap.gfood.api.core.domain.ApiPageResponse;
import br.com.fiap.gfood.api.core.domain.ApiResponse;
import br.com.fiap.gfood.api.presentation.models.CreateCustomerRequest;
import br.com.fiap.gfood.api.presentation.models.UpdateCustomerRequest;

@Service
public interface CustomerService
{
	ApiPageResponse findAllFiltering(String fullName, Pageable pageable);

	ApiResponse create(CreateCustomerRequest payload);

	ApiResponse update(UUID id, UpdateCustomerRequest payload);
}
