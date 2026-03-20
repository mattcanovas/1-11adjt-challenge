package br.com.fiap.gfood.api.application.usecase;

import static org.apache.commons.lang3.StringUtils.isBlank;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.fiap.gfood.api.domain.gateway.CustomerGateway;
import br.com.fiap.gfood.api.domain.model.Customer;

@Service
public class FindCustomersUseCase
{
	private final CustomerGateway gateway;

	public FindCustomersUseCase(CustomerGateway gateway)
	{
		this.gateway = gateway;
	}

	public Page<Customer> execute(String fullName, Pageable pageable)
	{
		return isBlank(fullName)
				? gateway.findAll(pageable)
				: gateway.findByFullNameContaining(fullName, pageable);
	}
}
