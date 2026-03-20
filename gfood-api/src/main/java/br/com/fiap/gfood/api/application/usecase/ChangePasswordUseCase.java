package br.com.fiap.gfood.api.application.usecase;

import java.util.UUID;

import org.springframework.stereotype.Service;

import br.com.fiap.gfood.api.application.dto.ChangePasswordRequest;
import br.com.fiap.gfood.api.domain.exception.CustomerNotFoundException;
import br.com.fiap.gfood.api.domain.exception.PasswordConfirmationMismatchException;
import br.com.fiap.gfood.api.domain.exception.PasswordMismatchException;
import br.com.fiap.gfood.api.domain.gateway.CustomerGateway;
import br.com.fiap.gfood.api.domain.model.Customer;

@Service
public class ChangePasswordUseCase
{
	private static final String MESSAGE_ERROR_CUSTOMER_NOT_FOUND = "The customer requested was not found.";
	private static final String THE_OLD_PASSWORD_MISMATCH = "The old password informed mismatch";
	private static final String THE_CONFIRMATION_OF_NEW_PASSWORD_MISMATCH = "The confirmation password mismatched.";

	private final CustomerGateway gateway;

	public ChangePasswordUseCase(CustomerGateway gateway)
	{
		this.gateway = gateway;
	}

	public Customer execute(UUID id, ChangePasswordRequest request)
	{
		Customer customer = gateway.findById(id)
				.orElseThrow(() -> new CustomerNotFoundException(MESSAGE_ERROR_CUSTOMER_NOT_FOUND));

		if (!request.oldPassword().equals(customer.getPassword()))
		{
			throw new PasswordMismatchException(THE_OLD_PASSWORD_MISMATCH);
		}
		if (!request.newPassword().equals(request.confirmPassword()))
		{
			throw new PasswordConfirmationMismatchException(THE_CONFIRMATION_OF_NEW_PASSWORD_MISMATCH);
		}

		customer.setPassword(request.newPassword());
		return gateway.save(customer);
	}
}
