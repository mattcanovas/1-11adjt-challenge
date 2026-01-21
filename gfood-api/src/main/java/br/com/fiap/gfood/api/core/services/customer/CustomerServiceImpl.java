package br.com.fiap.gfood.api.core.services.customer;

import static br.com.fiap.gfood.api.core.commons.Constants.MESSAGE_ERROR_EMAIL_IS_ALREADY_USED;
import static org.apache.commons.lang3.StringUtils.isBlank;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import br.com.fiap.gfood.api.core.domain.ApiPageResponse;
import br.com.fiap.gfood.api.core.domain.ApiResponse;
import br.com.fiap.gfood.api.core.domain.Customer;
import br.com.fiap.gfood.api.core.exceptions.EmailAlreadyUsedException;
import br.com.fiap.gfood.api.core.exceptions.GlobalExceptionHandler;
import br.com.fiap.gfood.api.core.parsers.customer.CustomerParser;
import br.com.fiap.gfood.api.data.entities.CustomerData;
import br.com.fiap.gfood.api.data.repositories.CustomerRepository;
import br.com.fiap.gfood.api.presentation.models.CreateCustomerRequest;

@Component
public class CustomerServiceImpl implements CustomerService
{

	private final GlobalExceptionHandler globalExceptionHandler;
	private final CustomerRepository repository;
	private final CustomerParser parser;

	public CustomerServiceImpl(CustomerRepository repository, CustomerParser parser,
			GlobalExceptionHandler globalExceptionHandler)
	{
		this.repository = repository;
		this.parser = parser;
		this.globalExceptionHandler = globalExceptionHandler;
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

}
