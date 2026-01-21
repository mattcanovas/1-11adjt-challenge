package br.com.fiap.gfood.api.core.parsers.customer;

import org.springframework.stereotype.Component;

import br.com.fiap.gfood.api.core.domain.Customer;
import br.com.fiap.gfood.api.data.entities.CustomerData;

@Component
public class CustomerParserImpl implements CustomerParser
{

	@Override
	public Customer toDomain(CustomerData entity)
	{
		return Customer.builder().id(entity.getId()).fullName(entity.getFullName()).email(entity.getEmail())
				.login(entity.getLogin()).createdAt(entity.getCreatedAt())
				.updatedAt(entity.getUpdatedAt()).build();
	}

}
