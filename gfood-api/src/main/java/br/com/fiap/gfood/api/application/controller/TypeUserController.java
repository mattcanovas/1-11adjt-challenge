package br.com.fiap.gfood.api.application.controller;

import static org.springframework.data.domain.PageRequest.of;
import static org.springframework.data.domain.Sort.by;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import br.com.fiap.gfood.api.application.dto.ApiPageResponse;
import br.com.fiap.gfood.api.application.dto.ApiResponse;
import br.com.fiap.gfood.api.application.dto.CreateTypeUserRequest;
import br.com.fiap.gfood.api.application.dto.UpdateTypeUserRequest;
import br.com.fiap.gfood.api.application.usecase.CreateTypeUserUseCase;
import br.com.fiap.gfood.api.application.usecase.DeleteTypeUserUseCase;
import br.com.fiap.gfood.api.application.usecase.FindTypeUsersUseCase;
import br.com.fiap.gfood.api.application.usecase.UpdateTypeUserUseCase;
import br.com.fiap.gfood.api.domain.model.TypeUser;

@Component
public class TypeUserController implements TypeUserResource
{
	private static final String CREATED_AT_SORT_PARAMETER = "createdAt";

	private final CreateTypeUserUseCase createTypeUserUseCase;
	private final FindTypeUsersUseCase findTypeUsersUseCase;
	private final UpdateTypeUserUseCase updateTypeUserUseCase;
	private final DeleteTypeUserUseCase deleteTypeUserUseCase;

	public TypeUserController(CreateTypeUserUseCase createTypeUserUseCase,
			FindTypeUsersUseCase findTypeUsersUseCase,
			UpdateTypeUserUseCase updateTypeUserUseCase,
			DeleteTypeUserUseCase deleteTypeUserUseCase)
	{
		this.createTypeUserUseCase = createTypeUserUseCase;
		this.findTypeUsersUseCase = findTypeUsersUseCase;
		this.updateTypeUserUseCase = updateTypeUserUseCase;
		this.deleteTypeUserUseCase = deleteTypeUserUseCase;
	}

	@Override
	public ResponseEntity<ApiPageResponse> findAllFiltering(String name, Integer page, Integer size)
	{
		return ResponseEntity.ok(new ApiPageResponse(Boolean.TRUE,
				findTypeUsersUseCase.execute(name, of(page, size, by(CREATED_AT_SORT_PARAMETER)))));
	}

	@Override
	public ResponseEntity<ApiResponse> findById(UUID id)
	{
		TypeUser typeUser = findTypeUsersUseCase.executeById(id);
		return ResponseEntity.ok(new ApiResponse(Boolean.TRUE, typeUser));
	}

	@Override
	public ResponseEntity<ApiResponse> create(CreateTypeUserRequest payload)
	{
		TypeUser typeUser = createTypeUserUseCase.execute(payload);
		return new ResponseEntity<>(new ApiResponse(Boolean.TRUE, typeUser), HttpStatus.CREATED);
	}

	@Override
	public ResponseEntity<ApiResponse> update(UUID id, UpdateTypeUserRequest payload)
	{
		TypeUser typeUser = updateTypeUserUseCase.execute(id, payload);
		return new ResponseEntity<>(new ApiResponse(Boolean.TRUE, typeUser), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<Object> delete(UUID id)
	{
		deleteTypeUserUseCase.execute(id);
		return new ResponseEntity<>(HttpStatus.ACCEPTED);
	}
}
