package br.com.fiap.gfood.api.application.controller;

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

import br.com.fiap.gfood.api.application.dto.ApiPageResponse;
import br.com.fiap.gfood.api.application.dto.ApiResponse;
import br.com.fiap.gfood.api.application.dto.ChangePasswordRequest;
import br.com.fiap.gfood.api.application.dto.CreateCustomerRequest;
import br.com.fiap.gfood.api.application.dto.ProblemDetail;
import br.com.fiap.gfood.api.application.dto.SignInRequest;
import br.com.fiap.gfood.api.application.dto.UpdateCustomerRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;

@RestController
@RequestMapping(value = "/v1/customer")
@Tag(name = "Customer", description = "Customer's Controller")
public interface CustomerResource
{
	@Operation(
		summary = "Find all customers",
		description = "Retrieves a paginated list of customers, optionally filtering by first name."
	)
	@GetMapping(produces = { "application/json", "application/problem+json" })
	@ApiResponses({
		@io.swagger.v3.oas.annotations.responses.ApiResponse(
			responseCode = "200",
			description = "Successfull response",
			content = @Content(
				mediaType = "application/json",
				schema = @Schema(implementation = ApiPageResponse.class),
				examples = @ExampleObject(value = """
					{
					    "success": true,
					    "page": {
					        "content": [
					            {
					                "id": "a327963f-545e-4414-896d-a2ab832dd147",
					                "fullName": "Matheus Canovas Almeida Santos",
					                "login": "matheus.canovas",
					                "email": "matheuscanovas9@gmail.com",
					                "type": "OWNER",
					                "createdAt": "2026-01-21T17:28:26.761502",
					                "updatedAt": "2026-01-21T17:28:26.761502",
					                "address": "Rua Basilio da Cunha 889, Apto. 122"
					            }
					        ],
					        "pageable": {
					            "pageNumber": 0,
					            "pageSize": 10,
					            "sort": {
					                "empty": false,
					                "sorted": true,
					                "unsorted": false
					            },
					            "offset": 0,
					            "paged": true,
					            "unpaged": false
					        },
					        "last": true,
					        "totalPages": 1,
					        "totalElements": 1,
					        "size": 10,
					        "number": 0,
					        "sort": {
					            "empty": false,
					            "sorted": true,
					            "unsorted": false
					        },
					        "first": true,
					        "numberOfElements": 1,
					        "empty": false
					    }
					}
					"""
				)
			)
		)
	})
	ResponseEntity<ApiPageResponse> findAllFiltering(
			@Parameter(description = "Filter customers by first name (partial match)", example = "Matheus")
			@RequestParam(required = false) String firstName,
			@Parameter(description = "Page number (zero-based)", example = "0")
			@RequestParam(defaultValue = "0") Integer page,
			@Parameter(description = "Number of records per page", example = "10")
			@RequestParam(defaultValue = "10") Integer size);

	@Operation(
		summary = "Create a new customer",
		description = "Registers a new customer in the system. The email must be unique."
	)
	@PostMapping(produces = { "application/json", "application/problem+json" })
	@ApiResponses({
		@io.swagger.v3.oas.annotations.responses.ApiResponse(
			responseCode = "201",
			description = "Customer created successfully",
			content = @Content(
				mediaType = "application/json",
				schema = @Schema(implementation = ApiResponse.class),
				examples = @ExampleObject(value = """
					{
					    "success": true,
					    "data": {
					        "id": "809ada66-68cf-440b-87ee-88fb17730c84",
					        "fullName": "Matheus Canovas Almeida Santos",
					        "login": "matheus.canovas",
					        "email": "teste123@gmail.com",
					        "type": "OWNER",
					        "createdAt": "2026-01-21T18:18:54.8204677",
					        "updatedAt": "2026-01-21T18:18:54.8210539",
					        "address": "Rua Basilio da Cunha 889, Apto. 122"
					    }
					}
					"""
				)
			)
		),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(
			responseCode = "412",
			description = "Email is already used (validation)",
			content = @Content(
				mediaType = "application/problem+json",
				schema = @Schema(implementation = ProblemDetail.class),
				examples = @ExampleObject(value = """
					{
					    "success": false,
					    "type": "http://localhost:8080/v1/customer",
					    "title": "The email informed is used in another account.",
					    "details": "The email that was sended to register new customer is aleady used. Try another one.",
					    "errors": {
					        "email": "Email is already used."
					    }
					}
					"""
				)
			)
		),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(
			responseCode = "400",
			description = "Invalid request (validation / bad input)",
			content = @Content(
				mediaType = "application/problem+json",
				schema = @Schema(implementation = ProblemDetail.class),
				examples = @ExampleObject(value = """
					{
					    "success": false,
					    "type": "http://localhost:8080/v1/customer",
					    "title": "Your request parameters didn't validate",
					    "details": "The parameters that was send in request's body is not valid according with business rules.",
					    "errors": {
					        "password": "The password must be informed.",
					        "address": "The address must be informed.",
					        "fullName": "The full name must be informed.",
					        "type": "The type must be informed.",
					        "login": "The login must be informed.",
					        "email": "The email must be informed"
					    }
					}
					"""
				)
			)
		)
	})
	ResponseEntity<ApiResponse> create(
			@io.swagger.v3.oas.annotations.parameters.RequestBody(
				description = "Customer data to create",
				required = true
			)
			@RequestBody @Validated CreateCustomerRequest payload);

	@Operation(
		summary = "Update an existing customer",
		description = "Updates the data of an existing customer identified by UUID."
	)
	@PutMapping(value = "/{id}", produces = { "application/json", "application/problem+json" })
	@ApiResponses({
		@io.swagger.v3.oas.annotations.responses.ApiResponse(
			responseCode = "200",
			description = "Customer updated successfully",
			content = @Content(
				mediaType = "application/json",
				schema = @Schema(implementation = ApiResponse.class),
				examples = @ExampleObject(value = """
					{
					    "success": true,
					    "data": {
					        "id": "809ada66-68cf-440b-87ee-88fb17730c84",
					        "fullName": "Matheus Canovas Almeida Santos",
					        "login": "matheus.canovas",
					        "email": "teste123@gmail.com",
					        "type": "OWNER",
					        "createdAt": "2026-01-21T18:18:54.8204677",
					        "updatedAt": "2026-01-21T18:18:54.8210539",
					        "address": "Rua Basilio da Cunha 889, Apto. 122"
					    }
					}
					"""
				)
			)
		),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(
			responseCode = "412",
			description = "Email is already used (validation)",
			content = @Content(
				mediaType = "application/problem+json",
				schema = @Schema(implementation = ProblemDetail.class),
				examples = @ExampleObject(value = """
					{
					    "success": false,
					    "type": "http://localhost:8080/v1/customer",
					    "title": "The email informed is used in another account.",
					    "details": "The email that was sended to register new customer is aleady used. Try another one.",
					    "errors": {
					        "email": "Email is already used."
					    }
					}
					"""
				)
			)
		),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(
			responseCode = "400",
			description = "Invalid request (validation / bad input)",
			content = @Content(
				mediaType = "application/problem+json",
				schema = @Schema(implementation = ProblemDetail.class),
				examples = @ExampleObject(value = """
					{
					    "success": false,
					    "type": "http://localhost:8080/v1/customer",
					    "title": "Your request parameters didn't validate",
					    "details": "The parameters that was send in request's body is not valid according with business rules.",
					    "errors": {
					        "password": "The password must be informed.",
					        "address": "The address must be informed.",
					        "fullName": "The full name must be informed.",
					        "type": "The type must be informed.",
					        "login": "The login must be informed.",
					        "email": "The email must be informed"
					    }
					}
					"""
				)
			)
		)
	})
	ResponseEntity<ApiResponse> update(
			@Parameter(description = "UUID of the customer to update", required = true, example = "809ada66-68cf-440b-87ee-88fb17730c84")
			@PathVariable @NotNull(message = "The id of customer must be informed") UUID id,
			@io.swagger.v3.oas.annotations.parameters.RequestBody(
				description = "Customer data to update",
				required = true
			)
			@RequestBody @Validated UpdateCustomerRequest payload);

	@Operation(
		summary = "Delete a customer",
		description = "Asynchronously deletes a customer identified by UUID. Returns 202 Accepted."
	)
	@DeleteMapping(value = "/{id}", produces = { "application/json", "application/problem+json" })
	@ApiResponses({
		@io.swagger.v3.oas.annotations.responses.ApiResponse(
			responseCode = "202",
			description = "Deletion accepted"
		)
	})
	ResponseEntity<Object> delete(
			@Parameter(description = "UUID of the customer to delete", required = true, example = "809ada66-68cf-440b-87ee-88fb17730c84")
			@PathVariable @NotNull(message = "The id of customer must be informed") UUID id);

	@Operation(
		summary = "Change customer password",
		description = "Changes the password of an existing customer. Requires the old password for validation and the new password with confirmation."
	)
	@PatchMapping(value = "/change-password/{id}", produces = { "application/json", "application/problem+json" })
	@ApiResponses({
		@io.swagger.v3.oas.annotations.responses.ApiResponse(
			responseCode = "200",
			description = "Password changed successfully",
			content = @Content(
				mediaType = "application/json",
				schema = @Schema(implementation = ApiResponse.class),
				examples = @ExampleObject(value = """
					{
					    "success": true,
					    "data": {
					        "id": "a327963f-545e-4414-896d-a2ab832dd147",
					        "fullName": "Matheus Canovas Almeida Santos",
					        "login": "matheus.canovas",
					        "email": "matheuscanovas9@gmail.com",
					        "type": "OWNER",
					        "createdAt": "2026-01-21T17:28:26.761502",
					        "updatedAt": "2026-01-21T18:38:37.5079238",
					        "address": "Rua Basilio da Cunha 889, Apto. 122"
					    }
					}
					"""
				)
			)
		),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(
			responseCode = "400",
			description = "Invalid input (validation / bad input)",
			content = @Content(
				mediaType = "application/problem+json",
				schema = @Schema(implementation = ProblemDetail.class),
				examples = @ExampleObject(value = """
					{
					    "success": false,
					    "type": "http://localhost:8080/v1/customer/change-password/a327963f-545e-4414-896d-a2ab832dd147",
					    "title": "Your request parameters didn't validate",
					    "details": "The parameters that was send in request's body is not valid according with business rules.",
					    "errors": {
					        "oldPassword": "The old password must be informed."
					    }
					}
					"""
				)
			)
		),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(
			responseCode = "412",
			description = "Precondition failed (password mismatch)",
			content = @Content(
				mediaType = "application/problem+json",
				schema = @Schema(implementation = ProblemDetail.class),
				examples = @ExampleObject(value = """
					{
					    "success": false,
					    "type": "http://localhost:8080/v1/customer/change-password/a327963f-545e-4414-896d-a2ab832dd147",
					    "title": "The old password informed mismatch",
					    "details": "The old password informed on the body requisition mismatched with the real password.",
					    "errors": {
					        "old_password": "The old password informed mismatch"
					    }
					}
					"""
				)
			)
		)
	})
	ResponseEntity<Object> changePassword(
			@Parameter(description = "UUID of the customer", required = true, example = "a327963f-545e-4414-896d-a2ab832dd147")
			@PathVariable @NotNull(message = "The id of customer must be informed") UUID id,
			@io.swagger.v3.oas.annotations.parameters.RequestBody(
				description = "Old password, new password and confirmation",
				required = true
			)
			@RequestBody @Validated ChangePasswordRequest payload);

	@Operation(
		summary = "Sign in",
		description = "Authenticates a customer using login and password credentials."
	)
	@PostMapping(value = "/signin", produces = { "application/json", "application/problem+json" })
	@ApiResponses({
		@io.swagger.v3.oas.annotations.responses.ApiResponse(
			responseCode = "200",
			description = "Authentication successful",
			content = @Content(
				mediaType = "application/json",
				schema = @Schema(implementation = ApiResponse.class),
				examples = @ExampleObject(value = """
					{ "success": true, "data": true }
					"""
				)
			)
		),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(
			responseCode = "412",
			description = "Invalid authentication",
			content = @Content(
				mediaType = "application/problem+json",
				schema = @Schema(implementation = ProblemDetail.class),
				examples = @ExampleObject(value = """
					{
					    "success": false,
					    "type": "http://localhost:8080/v1/customer/signin",
					    "title": "Invalid login and password.",
					    "details": "The login and password informed in the request body are invalid. Try again",
					    "errors": {
					        "password": "Invalid login and password.",
					        "login": "Invalid login and password."
					    }
					}
					"""
				)
			)
		)
	})
	ResponseEntity<Object> signIn(
			@io.swagger.v3.oas.annotations.parameters.RequestBody(
				description = "Login credentials",
				required = true
			)
			@RequestBody @Validated SignInRequest payload);
}
