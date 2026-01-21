package br.com.fiap.gfood.api.core.parsers.customer;

import org.springframework.stereotype.Component;

import br.com.fiap.gfood.api.core.domain.Customer;
import br.com.fiap.gfood.api.core.parsers.ToDomainParser;
import br.com.fiap.gfood.api.data.entities.CustomerData;

@Component
public interface CustomerParser extends ToDomainParser<Customer, CustomerData>
{
}
