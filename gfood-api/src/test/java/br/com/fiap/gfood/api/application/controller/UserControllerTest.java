package br.com.fiap.gfood.api.application.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

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
import br.com.fiap.gfood.api.domain.exception.AuthenticationFailedException;
import br.com.fiap.gfood.api.domain.exception.EmailAlreadyUsedException;
import br.com.fiap.gfood.api.domain.exception.PasswordMismatchException;
import br.com.fiap.gfood.api.domain.exception.UserNotFoundException;
import br.com.fiap.gfood.api.domain.model.TypeUser;
import br.com.fiap.gfood.api.domain.model.User;

@WebMvcTest(UserResource.class)
class UserControllerTest
{
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private CreateUserUseCase createUserUseCase;

	@MockBean
	private FindUsersUseCase findUsersUseCase;

	@MockBean
	private UpdateUserUseCase updateUserUseCase;

	@MockBean
	private DeleteUserUseCase deleteUserUseCase;

	@MockBean
	private ChangePasswordUseCase changePasswordUseCase;

	@MockBean
	private SignInUseCase signInUseCase;

	// --- GET /v1/user ---

	@Test
	void shouldFindAllUsers() throws Exception
	{
		User user = User.builder().id(UUID.randomUUID()).fullName("John Doe").build();
		when(findUsersUseCase.execute(any(), any(Pageable.class)))
				.thenReturn(new PageImpl<>(List.of(user)));

		mockMvc.perform(get("/v1/user"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.success").value(true))
				.andExpect(jsonPath("$.page.content[0].fullName").value("John Doe"));
	}

	@Test
	void shouldFindUsersWithNameFilter() throws Exception
	{
		User user = User.builder().id(UUID.randomUUID()).fullName("John Doe").build();
		when(findUsersUseCase.execute(any(), any(Pageable.class)))
				.thenReturn(new PageImpl<>(List.of(user)));

		mockMvc.perform(get("/v1/user").param("firstName", "John"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.success").value(true))
				.andExpect(jsonPath("$.page.content[0].fullName").value("John Doe"));
	}

	// --- POST /v1/user ---

	@Test
	void shouldCreateUser() throws Exception
	{
		UUID typeUserId = UUID.randomUUID();
		CreateUserRequest request = new CreateUserRequest("John Doe", "john@test.com", "john.doe", "pass123", typeUserId, "Rua A 123");
		User savedUser = User.builder().id(UUID.randomUUID()).fullName("John Doe").email("john@test.com").build();

		when(createUserUseCase.execute(any(CreateUserRequest.class))).thenReturn(savedUser);

		mockMvc.perform(post("/v1/user")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.success").value(true))
				.andExpect(jsonPath("$.data.fullName").value("John Doe"));
	}

	@Test
	void shouldReturn400WhenCreateUserWithInvalidBody() throws Exception
	{
		String invalidBody = "{}";

		mockMvc.perform(post("/v1/user")
						.contentType(MediaType.APPLICATION_JSON)
						.content(invalidBody))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.success").value(false));
	}

	@Test
	void shouldReturn412WhenEmailAlreadyUsed() throws Exception
	{
		UUID typeUserId = UUID.randomUUID();
		CreateUserRequest request = new CreateUserRequest("John Doe", "john@test.com", "john.doe", "pass123", typeUserId, "Rua A 123");

		when(createUserUseCase.execute(any(CreateUserRequest.class)))
				.thenThrow(new EmailAlreadyUsedException("Email is already used."));

		mockMvc.perform(post("/v1/user")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isPreconditionFailed())
				.andExpect(jsonPath("$.success").value(false));
	}

	// --- PUT /v1/user/{id} ---

	@Test
	void shouldUpdateUser() throws Exception
	{
		UUID userId = UUID.randomUUID();
		UUID typeUserId = UUID.randomUUID();
		UpdateUserRequest request = new UpdateUserRequest("John Doe Updated", "john@test.com", "john.doe", typeUserId, "Rua B 456");
		User updatedUser = User.builder().id(userId).fullName("John Doe Updated").build();

		when(updateUserUseCase.execute(eq(userId), any(UpdateUserRequest.class))).thenReturn(updatedUser);

		mockMvc.perform(put("/v1/user/{id}", userId)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.success").value(true))
				.andExpect(jsonPath("$.data.fullName").value("John Doe Updated"));
	}

	@Test
	void shouldReturn412WhenUpdateUserNotFound() throws Exception
	{
		UUID userId = UUID.randomUUID();
		UUID typeUserId = UUID.randomUUID();
		UpdateUserRequest request = new UpdateUserRequest("John Doe", "john@test.com", "john.doe", typeUserId, "Rua A 123");

		when(updateUserUseCase.execute(eq(userId), any(UpdateUserRequest.class)))
				.thenThrow(new UserNotFoundException("User not found"));

		mockMvc.perform(put("/v1/user/{id}", userId)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isPreconditionFailed())
				.andExpect(jsonPath("$.success").value(false));
	}

	// --- DELETE /v1/user/{id} ---

	@Test
	void shouldDeleteUser() throws Exception
	{
		UUID userId = UUID.randomUUID();
		doNothing().when(deleteUserUseCase).execute(userId);

		mockMvc.perform(delete("/v1/user/{id}", userId))
				.andExpect(status().isAccepted());

		verify(deleteUserUseCase).execute(userId);
	}

	// --- PATCH /v1/user/change-password/{id} ---

	@Test
	void shouldChangePassword() throws Exception
	{
		UUID userId = UUID.randomUUID();
		ChangePasswordRequest request = new ChangePasswordRequest("oldPass", "newPass", "newPass");
		User user = User.builder().id(userId).fullName("John Doe").password("newPass").build();

		when(changePasswordUseCase.execute(eq(userId), any(ChangePasswordRequest.class))).thenReturn(user);

		mockMvc.perform(patch("/v1/user/change-password/{id}", userId)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.success").value(true));
	}

	@Test
	void shouldReturn412WhenOldPasswordMismatch() throws Exception
	{
		UUID userId = UUID.randomUUID();
		ChangePasswordRequest request = new ChangePasswordRequest("wrongOld", "newPass", "newPass");

		when(changePasswordUseCase.execute(eq(userId), any(ChangePasswordRequest.class)))
				.thenThrow(new PasswordMismatchException("The old password informed mismatch"));

		mockMvc.perform(patch("/v1/user/change-password/{id}", userId)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isPreconditionFailed())
				.andExpect(jsonPath("$.success").value(false));
	}

	@Test
	void shouldReturn400WhenChangePasswordWithInvalidBody() throws Exception
	{
		UUID userId = UUID.randomUUID();

		mockMvc.perform(patch("/v1/user/change-password/{id}", userId)
						.contentType(MediaType.APPLICATION_JSON)
						.content("{}"))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.success").value(false));
	}

	// --- POST /v1/user/signin ---

	@Test
	void shouldSignInSuccessfully() throws Exception
	{
		SignInRequest request = new SignInRequest("john.doe", "pass123");
		doNothing().when(signInUseCase).execute(any(SignInRequest.class));

		mockMvc.perform(post("/v1/user/signin")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.success").value(true))
				.andExpect(jsonPath("$.data").value(true));
	}

	@Test
	void shouldReturn412WhenSignInFails() throws Exception
	{
		SignInRequest request = new SignInRequest("john.doe", "wrongPass");

		doThrow(new AuthenticationFailedException("Invalid login and password."))
				.when(signInUseCase).execute(any(SignInRequest.class));

		mockMvc.perform(post("/v1/user/signin")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isPreconditionFailed())
				.andExpect(jsonPath("$.success").value(false));
	}

	@Test
	void shouldReturn400WhenSignInWithInvalidBody() throws Exception
	{
		mockMvc.perform(post("/v1/user/signin")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{}"))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.success").value(false));
	}
}
