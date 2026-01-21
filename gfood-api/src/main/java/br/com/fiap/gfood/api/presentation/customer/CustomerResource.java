package br.com.fiap.gfood.api.presentation.customer;

import static br.com.fiap.gfood.api.core.commons.Constants.V1_CUSTOMER;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.fiap.gfood.api.core.domain.ApiPageResponse;
import br.com.fiap.gfood.api.core.domain.ApiResponse;
import br.com.fiap.gfood.api.core.domain.ChangePasswordRequest;
import br.com.fiap.gfood.api.presentation.models.CreateCustomerRequest;
import br.com.fiap.gfood.api.presentation.models.SignInRequest;
import br.com.fiap.gfood.api.presentation.models.UpdateCustomerRequest;
import jakarta.validation.constraints.NotNull;

@RestController
@RequestMapping(value = V1_CUSTOMER)
public interface CustomerResource
{
	@GetMapping
	ResponseEntity<ApiPageResponse> findAllFiltering(@RequestParam(required = false) String firstName,
			@RequestParam(defaultValue = "0") Integer page,
			@RequestParam(defaultValue = "10") Integer size);

	@PostMapping
	ResponseEntity<ApiResponse> create(@RequestBody @Validated CreateCustomerRequest payload);

	@PutMapping("/{id}")
	ResponseEntity<ApiResponse> update(
			@PathVariable @NotNull(message = "The id of customer must be informed") UUID id,
			@RequestBody @Validated UpdateCustomerRequest payload);

	@DeleteMapping("/{id}")
	ResponseEntity<Object> delete(@PathVariable @NotNull(message = "The id of customer must be informed") UUID id);

	@PatchMapping("/change-password/{id}")
	ResponseEntity<Object> changePassword(@PathVariable @NotNull(message = "The id of customer must be informed") UUID id, @RequestBody @Validated ChangePasswordRequest payload);
	
	@PostMapping("/signin")
	ResponseEntity<Object> signIn(@RequestBody @Validated SignInRequest payload);
}
