package br.com.fiap.gfood.api.application.usecase;

import static java.util.Objects.isNull;
import static org.apache.commons.lang3.StringUtils.isBlank;

import java.util.UUID;

import org.springframework.stereotype.Service;

import br.com.fiap.gfood.api.application.dto.UpdateCustomerRequest;
import br.com.fiap.gfood.api.domain.exception.CustomerNotFoundException;
import br.com.fiap.gfood.api.domain.exception.EmailAlreadyUsedException;
import br.com.fiap.gfood.api.domain.gateway.CustomerGateway;
import br.com.fiap.gfood.api.domain.model.Customer;

@Service
public class UpdateCustomerUseCase
{
	private static final String MESSAGE_ERROR_CUSTOMER_NOT_FOUND = "The customer requested was not found.";
	private static final String MESSAGE_ERROR_EMAIL_IS_ALREADY_USED = "Email is already used.";

	private final CustomerGateway gateway;

	public UpdateCustomerUseCase(CustomerGateway gateway)
	{
		this.gateway = gateway;
	}

	public Customer execute(UUID id, UpdateCustomerRequest request)
	{
		Customer customer = gateway.findById(id)
				.orElseThrow(() -> new CustomerNotFoundException(MESSAGE_ERROR_CUSTOMER_NOT_FOUND));

		if (gateway.existsByEmail(request.email()))
		{
			throw new EmailAlreadyUsedException(MESSAGE_ERROR_EMAIL_IS_ALREADY_USED);
		}

		customer.setFullName(isBlank(request.fullName()) ? customer.getFullName() : request.fullName());
		customer.setEmail(isBlank(request.email()) ? customer.getEmail() : request.email());
		customer.setLogin(isBlank(request.login()) ? customer.getLogin() : request.login());
		customer.setType(isNull(request.type()) ? customer.getType() : request.type());
		customer.setAddress(isBlank(request.address()) ? customer.getAddress() : request.address());

		return gateway.save(customer);
	}
}
