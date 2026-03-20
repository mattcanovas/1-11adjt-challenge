package br.com.fiap.gfood.api.application.controller;

import static org.springframework.data.domain.PageRequest.of;
import static org.springframework.data.domain.Sort.by;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import br.com.fiap.gfood.api.application.dto.ApiPageResponse;
import br.com.fiap.gfood.api.application.dto.ApiResponse;
import br.com.fiap.gfood.api.application.dto.ChangePasswordRequest;
import br.com.fiap.gfood.api.application.dto.CreateCustomerRequest;
import br.com.fiap.gfood.api.application.dto.SignInRequest;
import br.com.fiap.gfood.api.application.dto.UpdateCustomerRequest;
import br.com.fiap.gfood.api.application.usecase.ChangePasswordUseCase;
import br.com.fiap.gfood.api.application.usecase.CreateCustomerUseCase;
import br.com.fiap.gfood.api.application.usecase.DeleteCustomerUseCase;
import br.com.fiap.gfood.api.application.usecase.FindCustomersUseCase;
import br.com.fiap.gfood.api.application.usecase.SignInUseCase;
import br.com.fiap.gfood.api.application.usecase.UpdateCustomerUseCase;
import br.com.fiap.gfood.api.domain.model.Customer;

@Component
public class CustomerController implements CustomerResource
{
	private static final String CREATED_AT_SORT_PARAMETER = "createdAt";

	private final CreateCustomerUseCase createCustomerUseCase;
	private final FindCustomersUseCase findCustomersUseCase;
	private final UpdateCustomerUseCase updateCustomerUseCase;
	private final DeleteCustomerUseCase deleteCustomerUseCase;
	private final ChangePasswordUseCase changePasswordUseCase;
	private final SignInUseCase signInUseCase;

	public CustomerController(CreateCustomerUseCase createCustomerUseCase,
			FindCustomersUseCase findCustomersUseCase,
			UpdateCustomerUseCase updateCustomerUseCase,
			DeleteCustomerUseCase deleteCustomerUseCase,
			ChangePasswordUseCase changePasswordUseCase,
			SignInUseCase signInUseCase)
	{
		this.createCustomerUseCase = createCustomerUseCase;
		this.findCustomersUseCase = findCustomersUseCase;
		this.updateCustomerUseCase = updateCustomerUseCase;
		this.deleteCustomerUseCase = deleteCustomerUseCase;
		this.changePasswordUseCase = changePasswordUseCase;
		this.signInUseCase = signInUseCase;
	}

	@Override
	public ResponseEntity<ApiPageResponse> findAllFiltering(String fullName, Integer page, Integer size)
	{
		return ResponseEntity.ok(new ApiPageResponse(Boolean.TRUE,
				findCustomersUseCase.execute(fullName, of(page, size, by(CREATED_AT_SORT_PARAMETER)))));
	}

	@Override
	public ResponseEntity<ApiResponse> create(CreateCustomerRequest payload)
	{
		Customer customer = createCustomerUseCase.execute(payload);
		return new ResponseEntity<>(new ApiResponse(Boolean.TRUE, customer), HttpStatus.CREATED);
	}

	@Override
	public ResponseEntity<ApiResponse> update(UUID id, UpdateCustomerRequest payload)
	{
		Customer customer = updateCustomerUseCase.execute(id, payload);
		return new ResponseEntity<>(new ApiResponse(Boolean.TRUE, customer), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<Object> delete(UUID id)
	{
		deleteCustomerUseCase.execute(id);
		return new ResponseEntity<>(HttpStatus.ACCEPTED);
	}

	@Override
	public ResponseEntity<Object> changePassword(UUID id, ChangePasswordRequest payload)
	{
		Customer customer = changePasswordUseCase.execute(id, payload);
		return new ResponseEntity<>(new ApiResponse(Boolean.TRUE, customer), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<Object> signIn(SignInRequest payload)
	{
		signInUseCase.execute(payload);
		return new ResponseEntity<>(new ApiResponse(Boolean.TRUE, Boolean.TRUE), HttpStatus.OK);
	}
}
