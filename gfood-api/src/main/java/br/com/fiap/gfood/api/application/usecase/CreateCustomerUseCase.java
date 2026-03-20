package br.com.fiap.gfood.api.application.usecase;

import org.springframework.stereotype.Service;

import br.com.fiap.gfood.api.application.dto.CreateCustomerRequest;
import br.com.fiap.gfood.api.domain.exception.EmailAlreadyUsedException;
import br.com.fiap.gfood.api.domain.gateway.CustomerGateway;
import br.com.fiap.gfood.api.domain.model.Customer;

@Service
public class CreateCustomerUseCase
{
	private static final String MESSAGE_ERROR_EMAIL_IS_ALREADY_USED = "Email is already used.";

	private final CustomerGateway gateway;

	public CreateCustomerUseCase(CustomerGateway gateway)
	{
		this.gateway = gateway;
	}

	public Customer execute(CreateCustomerRequest request)
	{
		if (gateway.existsByEmail(request.email()))
		{
			throw new EmailAlreadyUsedException(MESSAGE_ERROR_EMAIL_IS_ALREADY_USED);
		}
		Customer customer = Customer.builder()
				.fullName(request.fullName())
				.email(request.email())
				.login(request.login())
				.password(request.password())
				.type(request.type())
				.address(request.address())
				.build();
		return gateway.save(customer);
	}
}
