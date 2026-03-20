package br.com.fiap.gfood.api.data.gateway;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import br.com.fiap.gfood.api.data.entity.CustomerEntity;
import br.com.fiap.gfood.api.data.repository.CustomerRepository;
import br.com.fiap.gfood.api.domain.gateway.CustomerGateway;
import br.com.fiap.gfood.api.domain.model.Customer;

@Component
public class CustomerGatewayImpl implements CustomerGateway
{
	private final CustomerRepository repository;

	public CustomerGatewayImpl(CustomerRepository repository)
	{
		this.repository = repository;
	}

	@Override
	public Page<Customer> findAll(Pageable pageable)
	{
		return repository.findAll(pageable).map(this::toDomain);
	}

	@Override
	public Page<Customer> findByFullNameContaining(String fullName, Pageable pageable)
	{
		return repository.findAllByFullNameContaining(fullName, pageable).map(this::toDomain);
	}

	@Override
	public Customer save(Customer customer)
	{
		CustomerEntity entity = toEntity(customer);
		return toDomain(repository.save(entity));
	}

	@Override
	public Optional<Customer> findById(UUID id)
	{
		return repository.findById(id).map(this::toDomain);
	}

	@Override
	public void deleteById(UUID id)
	{
		repository.deleteById(id);
	}

	@Override
	public boolean existsByEmail(String email)
	{
		return repository.existsByEmail(email);
	}

	@Override
	public Optional<Customer> findByLoginAndPassword(String login, String password)
	{
		return repository.findByLoginAndPassword(login, password).map(this::toDomain);
	}

	private Customer toDomain(CustomerEntity entity)
	{
		return Customer.builder()
				.id(entity.getId())
				.fullName(entity.getFullName())
				.login(entity.getLogin())
				.password(entity.getPassword())
				.email(entity.getEmail())
				.type(entity.getType())
				.createdAt(entity.getCreatedAt())
				.updatedAt(entity.getUpdatedAt())
				.address(entity.getAddress())
				.build();
	}

	private CustomerEntity toEntity(Customer customer)
	{
		return CustomerEntity.builder()
				.id(customer.getId())
				.fullName(customer.getFullName())
				.login(customer.getLogin())
				.password(customer.getPassword())
				.email(customer.getEmail())
				.type(customer.getType())
				.createdAt(customer.getCreatedAt())
				.updatedAt(customer.getUpdatedAt())
				.address(customer.getAddress())
				.build();
	}
}
