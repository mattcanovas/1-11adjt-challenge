package br.com.fiap.gfood.api.core.services.customer;

import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.fiap.gfood.api.core.domain.ApiPageResponse;
import br.com.fiap.gfood.api.core.domain.ApiResponse;
import br.com.fiap.gfood.api.core.domain.ChangePasswordRequest;
import br.com.fiap.gfood.api.presentation.models.CreateCustomerRequest;
import br.com.fiap.gfood.api.presentation.models.UpdateCustomerRequest;
import jakarta.validation.constraints.NotNull;

@Service
public interface CustomerService
{
	ApiPageResponse findAllFiltering(String fullName, Pageable pageable);

	ApiResponse create(CreateCustomerRequest payload);

	ApiResponse update(UUID id, UpdateCustomerRequest payload);

	void deleteById(UUID id);

	ApiResponse changePassword(@NotNull(message = "THe id of customer must be informed") UUID id,
			ChangePasswordRequest payload);
}
