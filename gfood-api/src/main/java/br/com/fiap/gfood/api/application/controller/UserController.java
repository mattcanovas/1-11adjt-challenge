package br.com.fiap.gfood.api.application.controller;

import static org.springframework.data.domain.PageRequest.of;
import static org.springframework.data.domain.Sort.by;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import br.com.fiap.gfood.api.application.dto.ApiPageResponse;
import br.com.fiap.gfood.api.application.dto.ApiResponse;
import br.com.fiap.gfood.api.application.dto.ChangePasswordRequest;
import br.com.fiap.gfood.api.application.dto.CreateUserRequest;
import br.com.fiap.gfood.api.application.dto.SignInRequest;
import br.com.fiap.gfood.api.application.dto.UpdateUserRequest;
import br.com.fiap.gfood.api.application.usecase.ChangePasswordUseCase;
import br.com.fiap.gfood.api.application.usecase.CreateUserUseCase;
import br.com.fiap.gfood.api.application.usecase.DeleteUserUseCase;
import br.com.fiap.gfood.api.application.usecase.FindUsersUseCase;
import br.com.fiap.gfood.api.application.usecase.SignInUseCase;
import br.com.fiap.gfood.api.application.usecase.UpdateUserUseCase;
import br.com.fiap.gfood.api.domain.model.User;

@Component
public class UserController implements UserResource
{
	private static final String CREATED_AT_SORT_PARAMETER = "createdAt";

	private final CreateUserUseCase createUserUseCase;
	private final FindUsersUseCase findUsersUseCase;
	private final UpdateUserUseCase updateUserUseCase;
	private final DeleteUserUseCase deleteUserUseCase;
	private final ChangePasswordUseCase changePasswordUseCase;
	private final SignInUseCase signInUseCase;

	public UserController(CreateUserUseCase createUserUseCase,
			FindUsersUseCase findUsersUseCase,
			UpdateUserUseCase updateUserUseCase,
			DeleteUserUseCase deleteUserUseCase,
			ChangePasswordUseCase changePasswordUseCase,
			SignInUseCase signInUseCase)
	{
		this.createUserUseCase = createUserUseCase;
		this.findUsersUseCase = findUsersUseCase;
		this.updateUserUseCase = updateUserUseCase;
		this.deleteUserUseCase = deleteUserUseCase;
		this.changePasswordUseCase = changePasswordUseCase;
		this.signInUseCase = signInUseCase;
	}

	@Override
	public ResponseEntity<ApiPageResponse> findAllFiltering(String fullName, Integer page, Integer size)
	{
		return ResponseEntity.ok(new ApiPageResponse(Boolean.TRUE,
				findUsersUseCase.execute(fullName, of(page, size, by(CREATED_AT_SORT_PARAMETER)))));
	}

	@Override
	public ResponseEntity<ApiResponse> create(CreateUserRequest payload)
	{
		User user = createUserUseCase.execute(payload);
		return new ResponseEntity<>(new ApiResponse(Boolean.TRUE, user), HttpStatus.CREATED);
	}

	@Override
	public ResponseEntity<ApiResponse> update(UUID id, UpdateUserRequest payload)
	{
		User user = updateUserUseCase.execute(id, payload);
		return new ResponseEntity<>(new ApiResponse(Boolean.TRUE, user), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<Object> delete(UUID id)
	{
		deleteUserUseCase.execute(id);
		return new ResponseEntity<>(HttpStatus.ACCEPTED);
	}

	@Override
	public ResponseEntity<Object> changePassword(UUID id, ChangePasswordRequest payload)
	{
		User user = changePasswordUseCase.execute(id, payload);
		return new ResponseEntity<>(new ApiResponse(Boolean.TRUE, user), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<Object> signIn(SignInRequest payload)
	{
		signInUseCase.execute(payload);
		return new ResponseEntity<>(new ApiResponse(Boolean.TRUE, Boolean.TRUE), HttpStatus.OK);
	}
}
