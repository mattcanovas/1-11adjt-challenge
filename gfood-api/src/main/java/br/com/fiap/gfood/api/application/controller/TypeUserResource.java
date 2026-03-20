package br.com.fiap.gfood.api.application.controller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.fiap.gfood.api.application.dto.ApiPageResponse;
import br.com.fiap.gfood.api.application.dto.ApiResponse;
import br.com.fiap.gfood.api.application.dto.CreateTypeUserRequest;
import br.com.fiap.gfood.api.application.dto.ProblemDetail;
import br.com.fiap.gfood.api.application.dto.UpdateTypeUserRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;

@RestController
@RequestMapping(value = "/v1/type-user")
@Tag(name = "TypeUser", description = "Type User's Controller")
public interface TypeUserResource
{
	@Operation(
		summary = "Find all type users",
		description = "Retrieves a paginated list of type users, optionally filtering by name."
	)
	@GetMapping(produces = { "application/json", "application/problem+json" })
	@ApiResponses({
		@io.swagger.v3.oas.annotations.responses.ApiResponse(
			responseCode = "200",
			description = "Successful response",
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
					                "name": "DEFAULT",
					                "description": "Default user type",
					                "createdAt": "2026-01-21T17:28:26.761502",
					                "updatedAt": "2026-01-21T17:28:26.761502"
					            }
					        ],
					        "pageable": {
					            "pageNumber": 0,
					            "pageSize": 10
					        },
					        "totalPages": 1,
					        "totalElements": 1
					    }
					}
					"""
				)
			)
		)
	})
	ResponseEntity<ApiPageResponse> findAllFiltering(
			@Parameter(description = "Filter type users by name (partial match)", example = "DEFAULT")
			@RequestParam(required = false) String name,
			@Parameter(description = "Page number (zero-based)", example = "0")
			@RequestParam(defaultValue = "0") Integer page,
			@Parameter(description = "Number of records per page", example = "10")
			@RequestParam(defaultValue = "10") Integer size);

	@Operation(
		summary = "Find type user by ID",
		description = "Retrieves a single type user by its UUID."
	)
	@GetMapping(value = "/{id}", produces = { "application/json", "application/problem+json" })
	@ApiResponses({
		@io.swagger.v3.oas.annotations.responses.ApiResponse(
			responseCode = "200",
			description = "Successful response",
			content = @Content(
				mediaType = "application/json",
				schema = @Schema(implementation = ApiResponse.class)
			)
		),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(
			responseCode = "412",
			description = "Type user not found",
			content = @Content(
				mediaType = "application/problem+json",
				schema = @Schema(implementation = ProblemDetail.class)
			)
		)
	})
	ResponseEntity<ApiResponse> findById(
			@Parameter(description = "UUID of the type user", required = true)
			@PathVariable @NotNull(message = "The id of type user must be informed") UUID id);

	@Operation(
		summary = "Create a new type user",
		description = "Registers a new type user in the system. The name must be unique."
	)
	@PostMapping(produces = { "application/json", "application/problem+json" })
	@ApiResponses({
		@io.swagger.v3.oas.annotations.responses.ApiResponse(
			responseCode = "201",
			description = "Type user created successfully",
			content = @Content(
				mediaType = "application/json",
				schema = @Schema(implementation = ApiResponse.class)
			)
		),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(
			responseCode = "412",
			description = "Name is already used",
			content = @Content(
				mediaType = "application/problem+json",
				schema = @Schema(implementation = ProblemDetail.class)
			)
		),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(
			responseCode = "400",
			description = "Invalid request (validation / bad input)",
			content = @Content(
				mediaType = "application/problem+json",
				schema = @Schema(implementation = ProblemDetail.class)
			)
		)
	})
	ResponseEntity<ApiResponse> create(
			@io.swagger.v3.oas.annotations.parameters.RequestBody(
				description = "Type user data to create",
				required = true
			)
			@RequestBody @Validated CreateTypeUserRequest payload);

	@Operation(
		summary = "Update an existing type user",
		description = "Updates the data of an existing type user identified by UUID."
	)
	@PutMapping(value = "/{id}", produces = { "application/json", "application/problem+json" })
	@ApiResponses({
		@io.swagger.v3.oas.annotations.responses.ApiResponse(
			responseCode = "200",
			description = "Type user updated successfully",
			content = @Content(
				mediaType = "application/json",
				schema = @Schema(implementation = ApiResponse.class)
			)
		),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(
			responseCode = "412",
			description = "Type user not found or name already used",
			content = @Content(
				mediaType = "application/problem+json",
				schema = @Schema(implementation = ProblemDetail.class)
			)
		)
	})
	ResponseEntity<ApiResponse> update(
			@Parameter(description = "UUID of the type user to update", required = true)
			@PathVariable @NotNull(message = "The id of type user must be informed") UUID id,
			@io.swagger.v3.oas.annotations.parameters.RequestBody(
				description = "Type user data to update",
				required = true
			)
			@RequestBody @Validated UpdateTypeUserRequest payload);

	@Operation(
		summary = "Delete a type user",
		description = "Asynchronously deletes a type user identified by UUID. Returns 202 Accepted."
	)
	@DeleteMapping(value = "/{id}", produces = { "application/json", "application/problem+json" })
	@ApiResponses({
		@io.swagger.v3.oas.annotations.responses.ApiResponse(
			responseCode = "202",
			description = "Deletion accepted"
		)
	})
	ResponseEntity<Object> delete(
			@Parameter(description = "UUID of the type user to delete", required = true)
			@PathVariable @NotNull(message = "The id of type user must be informed") UUID id);
}
