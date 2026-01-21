package br.com.fiap.gfood.api.presentation.customer;

import static br.com.fiap.gfood.api.core.commons.Constants.V1_CUSTOMER;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.fiap.gfood.api.presentation.models.CreateCustomerRequest;

@RestController
@RequestMapping(value = V1_CUSTOMER)
public interface CustomerResource
{
	@GetMapping
	ResponseEntity<Object> findAllFiltering(@RequestParam(required = false) String firstName,
			@RequestParam(defaultValue = "0") Integer page,
			@RequestParam(defaultValue = "10") Integer size);

	@PostMapping
	ResponseEntity<Object> create(@RequestBody @Validated CreateCustomerRequest payload);
}
