package br.com.fiap.gfood.api.application.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

import br.com.fiap.gfood.api.application.dto.CreateTypeUserRequest;
import br.com.fiap.gfood.api.application.dto.UpdateTypeUserRequest;
import br.com.fiap.gfood.api.application.usecase.CreateTypeUserUseCase;
import br.com.fiap.gfood.api.application.usecase.DeleteTypeUserUseCase;
import br.com.fiap.gfood.api.application.usecase.FindTypeUsersUseCase;
import br.com.fiap.gfood.api.application.usecase.UpdateTypeUserUseCase;
import br.com.fiap.gfood.api.domain.exception.TypeUserNameAlreadyExistsException;
import br.com.fiap.gfood.api.domain.exception.TypeUserNotFoundException;
import br.com.fiap.gfood.api.domain.model.TypeUser;

@WebMvcTest(TypeUserResource.class)
class TypeUserControllerTest
{
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private CreateTypeUserUseCase createTypeUserUseCase;

	@MockBean
	private FindTypeUsersUseCase findTypeUsersUseCase;

	@MockBean
	private UpdateTypeUserUseCase updateTypeUserUseCase;

	@MockBean
	private DeleteTypeUserUseCase deleteTypeUserUseCase;

	// --- GET /v1/type-user ---

	@Test
	void shouldFindAllTypeUsers() throws Exception
	{
		TypeUser typeUser = TypeUser.builder().id(UUID.randomUUID()).name("ADMIN").description("Admin").build();
		when(findTypeUsersUseCase.execute(any(), any(Pageable.class)))
				.thenReturn(new PageImpl<>(List.of(typeUser)));

		mockMvc.perform(get("/v1/type-user"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.success").value(true))
				.andExpect(jsonPath("$.page.content[0].name").value("ADMIN"));
	}

	@Test
	void shouldFindTypeUsersWithNameFilter() throws Exception
	{
		TypeUser typeUser = TypeUser.builder().id(UUID.randomUUID()).name("ADMIN").build();
		when(findTypeUsersUseCase.execute(eq("ADM"), any(Pageable.class)))
				.thenReturn(new PageImpl<>(List.of(typeUser)));

		mockMvc.perform(get("/v1/type-user").param("name", "ADM"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.success").value(true))
				.andExpect(jsonPath("$.page.content[0].name").value("ADMIN"));
	}

	// --- GET /v1/type-user/{id} ---

	@Test
	void shouldFindTypeUserById() throws Exception
	{
		UUID id = UUID.randomUUID();
		TypeUser typeUser = TypeUser.builder().id(id).name("OWNER").description("Owner type").build();

		when(findTypeUsersUseCase.executeById(id)).thenReturn(typeUser);

		mockMvc.perform(get("/v1/type-user/{id}", id))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.success").value(true))
				.andExpect(jsonPath("$.data.name").value("OWNER"));
	}

	@Test
	void shouldReturn412WhenTypeUserNotFoundById() throws Exception
	{
		UUID id = UUID.randomUUID();

		when(findTypeUsersUseCase.executeById(id))
				.thenThrow(new TypeUserNotFoundException("Type user not found"));

		mockMvc.perform(get("/v1/type-user/{id}", id))
				.andExpect(status().isPreconditionFailed())
				.andExpect(jsonPath("$.success").value(false));
	}

	// --- POST /v1/type-user ---

	@Test
	void shouldCreateTypeUser() throws Exception
	{
		CreateTypeUserRequest request = new CreateTypeUserRequest("DEFAULT", "Default user type");
		TypeUser created = TypeUser.builder().id(UUID.randomUUID()).name("DEFAULT").description("Default user type").build();

		when(createTypeUserUseCase.execute(any(CreateTypeUserRequest.class))).thenReturn(created);

		mockMvc.perform(post("/v1/type-user")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.success").value(true))
				.andExpect(jsonPath("$.data.name").value("DEFAULT"));
	}

	@Test
	void shouldReturn400WhenCreateTypeUserWithInvalidBody() throws Exception
	{
		mockMvc.perform(post("/v1/type-user")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{}"))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.success").value(false));
	}

	@Test
	void shouldReturn412WhenTypeUserNameAlreadyExists() throws Exception
	{
		CreateTypeUserRequest request = new CreateTypeUserRequest("ADMIN", "Admin");

		when(createTypeUserUseCase.execute(any(CreateTypeUserRequest.class)))
				.thenThrow(new TypeUserNameAlreadyExistsException("Name already exists"));

		mockMvc.perform(post("/v1/type-user")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isPreconditionFailed())
				.andExpect(jsonPath("$.success").value(false));
	}

	// --- PUT /v1/type-user/{id} ---

	@Test
	void shouldUpdateTypeUser() throws Exception
	{
		UUID id = UUID.randomUUID();
		UpdateTypeUserRequest request = new UpdateTypeUserRequest("UPDATED", "Updated description");
		TypeUser updated = TypeUser.builder().id(id).name("UPDATED").description("Updated description").build();

		when(updateTypeUserUseCase.execute(eq(id), any(UpdateTypeUserRequest.class))).thenReturn(updated);

		mockMvc.perform(put("/v1/type-user/{id}", id)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.success").value(true))
				.andExpect(jsonPath("$.data.name").value("UPDATED"));
	}

	@Test
	void shouldReturn412WhenUpdateTypeUserNotFound() throws Exception
	{
		UUID id = UUID.randomUUID();
		UpdateTypeUserRequest request = new UpdateTypeUserRequest("UPDATED", "Updated description");

		when(updateTypeUserUseCase.execute(eq(id), any(UpdateTypeUserRequest.class)))
				.thenThrow(new TypeUserNotFoundException("Type user not found"));

		mockMvc.perform(put("/v1/type-user/{id}", id)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isPreconditionFailed())
				.andExpect(jsonPath("$.success").value(false));
	}

	// --- DELETE /v1/type-user/{id} ---

	@Test
	void shouldDeleteTypeUser() throws Exception
	{
		UUID id = UUID.randomUUID();
		doNothing().when(deleteTypeUserUseCase).execute(id);

		mockMvc.perform(delete("/v1/type-user/{id}", id))
				.andExpect(status().isAccepted());

		verify(deleteTypeUserUseCase).execute(id);
	}
}
