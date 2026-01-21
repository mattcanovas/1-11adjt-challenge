package br.com.fiap.gfood.api.core.services.customer;

import static br.com.fiap.gfood.api.core.commons.Constants.MESSAGE_ERROR_CUSTOMER_NOT_FOUND;
import static br.com.fiap.gfood.api.core.commons.Constants.MESSAGE_ERROR_EMAIL_IS_ALREADY_USED;
import static java.util.Objects.isNull;
import static org.apache.commons.lang3.StringUtils.isBlank;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import br.com.fiap.gfood.api.core.commons.Constants;
import br.com.fiap.gfood.api.core.domain.ApiPageResponse;
import br.com.fiap.gfood.api.core.domain.ApiResponse;
import br.com.fiap.gfood.api.core.domain.ChangePasswordRequest;
import br.com.fiap.gfood.api.core.domain.Customer;
import br.com.fiap.gfood.api.core.exceptions.EmailAlreadyUsedException;
import br.com.fiap.gfood.api.core.exceptions.PasswordConfirmationMismatchException;
import br.com.fiap.gfood.api.core.exceptions.PasswordMismatchException;
import br.com.fiap.gfood.api.core.parsers.customer.CustomerParser;
import br.com.fiap.gfood.api.data.entities.CustomerData;
import br.com.fiap.gfood.api.data.repositories.CustomerRepository;
import br.com.fiap.gfood.api.presentation.models.CreateCustomerRequest;
import br.com.fiap.gfood.api.presentation.models.UpdateCustomerRequest;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.constraints.NotNull;

@Component
public class CustomerServiceImpl implements CustomerService
{
	private final CustomerRepository repository;
	private final CustomerParser parser;

	public CustomerServiceImpl(CustomerRepository repository, CustomerParser parser)
	{
		this.repository = repository;
		this.parser = parser;
	}

	@Override
	public ApiPageResponse findAllFiltering(String fullName, Pageable pageable)
	{
		Page<CustomerData> customersData = isBlank(fullName) ? repository.findAll(pageable)
				: repository.findAllByFullNameContaining(fullName, pageable);
		return new ApiPageResponse(Boolean.TRUE, customersData.map(parser::toDomain));
	}

	@Override
	public ApiResponse create(CreateCustomerRequest payload)
	{
		if (repository.existsByEmail(payload.email()))
		{
			throw new EmailAlreadyUsedException(MESSAGE_ERROR_EMAIL_IS_ALREADY_USED);
		}
		CustomerData newCustomer = CustomerData.builder().fullName(payload.fullName()).email(payload.email())
				.login(payload.login()).password(payload.password()).type(payload.type()).build();
		Customer savedCustomer = parser.toDomain(repository.save(newCustomer));
		return new ApiResponse(Boolean.TRUE, savedCustomer);
	}

	@Override
	public ApiResponse update(UUID id, UpdateCustomerRequest payload)
	{
		CustomerData customer = repository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException(MESSAGE_ERROR_CUSTOMER_NOT_FOUND));
		if (repository.existsByEmail(payload.email()))
		{
			throw new EmailAlreadyUsedException(MESSAGE_ERROR_EMAIL_IS_ALREADY_USED);
		}
		customer.setFullName(isBlank(payload.fullName()) ? customer.getFullName() : payload.fullName());
		customer.setEmail(isBlank(payload.email()) ? customer.getEmail() : payload.email());
		customer.setLogin(isBlank(payload.login()) ? customer.getLogin() : payload.login());
		customer.setType(isNull(payload.type()) ? customer.getType() : payload.type());
		customer = repository.save(customer);
		return new ApiResponse(Boolean.TRUE, parser.toDomain(customer));
	}

	@Override
	public void deleteById(UUID id)
	{
		CompletableFuture.runAsync(() -> {
			repository.deleteById(id);
		});
	}

	@Override
	public ApiResponse changePassword(@NotNull(message = "THe id of customer must be informed") UUID id,
			ChangePasswordRequest payload)
	{
		CustomerData customer = repository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException(MESSAGE_ERROR_CUSTOMER_NOT_FOUND));
		if (!payload.oldPassword().equals(customer.getPassword()))
		{
			throw new PasswordMismatchException(Constants.THE_OLD_PASSWORD_MISMATCH);
		}
		if (!payload.newPassword().equals(payload.confirmPassword()))
		{
			throw new PasswordConfirmationMismatchException(
					Constants.THE_CONFIRMATION_OF_NEW_PASSWORD_MISMATCH);
		}
		customer.setPassword(payload.newPassword());
		Customer customerDomain = parser.toDomain(repository.save(customer));
		return new ApiResponse(Boolean.TRUE, customerDomain);
	}

}
