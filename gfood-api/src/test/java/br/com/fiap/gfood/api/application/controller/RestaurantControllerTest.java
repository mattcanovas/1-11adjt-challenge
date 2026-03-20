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

import br.com.fiap.gfood.api.application.dto.CreateRestaurantRequest;
import br.com.fiap.gfood.api.application.dto.UpdateRestaurantRequest;
import br.com.fiap.gfood.api.application.usecase.CreateRestaurantUseCase;
import br.com.fiap.gfood.api.application.usecase.DeleteRestaurantUseCase;
import br.com.fiap.gfood.api.application.usecase.FindRestaurantsUseCase;
import br.com.fiap.gfood.api.application.usecase.UpdateRestaurantUseCase;
import br.com.fiap.gfood.api.domain.enums.TypeKitchen;
import br.com.fiap.gfood.api.domain.exception.RestaurantNameAlreadyExistsException;
import br.com.fiap.gfood.api.domain.exception.RestaurantNotFoundException;
import br.com.fiap.gfood.api.domain.model.Restaurant;

@WebMvcTest(RestaurantResource.class)
class RestaurantControllerTest
{
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private CreateRestaurantUseCase createRestaurantUseCase;

	@MockBean
	private FindRestaurantsUseCase findRestaurantsUseCase;

	@MockBean
	private UpdateRestaurantUseCase updateRestaurantUseCase;

	@MockBean
	private DeleteRestaurantUseCase deleteRestaurantUseCase;

	// --- GET /v1/restaurant ---

	@Test
	void shouldFindAllRestaurants() throws Exception
	{
		Restaurant restaurant = Restaurant.builder().id(UUID.randomUUID()).name("Pizzaria").build();
		when(findRestaurantsUseCase.execute(any(), any(Pageable.class)))
				.thenReturn(new PageImpl<>(List.of(restaurant)));

		mockMvc.perform(get("/v1/restaurant"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.success").value(true))
				.andExpect(jsonPath("$.page.content[0].name").value("Pizzaria"));
	}

	@Test
	void shouldFindRestaurantsWithNameFilter() throws Exception
	{
		Restaurant restaurant = Restaurant.builder().id(UUID.randomUUID()).name("Pizzaria").build();
		when(findRestaurantsUseCase.execute(eq("Pizza"), any(Pageable.class)))
				.thenReturn(new PageImpl<>(List.of(restaurant)));

		mockMvc.perform(get("/v1/restaurant").param("name", "Pizza"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.page.content[0].name").value("Pizzaria"));
	}

	// --- GET /v1/restaurant/{id} ---

	@Test
	void shouldFindRestaurantById() throws Exception
	{
		UUID id = UUID.randomUUID();
		Restaurant restaurant = Restaurant.builder().id(id).name("Pizzaria").typeKitchen(TypeKitchen.FAST_FOOD).build();

		when(findRestaurantsUseCase.executeById(id)).thenReturn(restaurant);

		mockMvc.perform(get("/v1/restaurant/{id}", id))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.success").value(true))
				.andExpect(jsonPath("$.data.name").value("Pizzaria"));
	}

	@Test
	void shouldReturn412WhenRestaurantNotFoundById() throws Exception
	{
		UUID id = UUID.randomUUID();

		when(findRestaurantsUseCase.executeById(id))
				.thenThrow(new RestaurantNotFoundException("Restaurant not found"));

		mockMvc.perform(get("/v1/restaurant/{id}", id))
				.andExpect(status().isPreconditionFailed())
				.andExpect(jsonPath("$.success").value(false));
	}

	// --- POST /v1/restaurant ---

	@Test
	void shouldCreateRestaurant() throws Exception
	{
		UUID ownerId = UUID.randomUUID();
		CreateRestaurantRequest request = new CreateRestaurantRequest("Pizzaria", "Rua A 123", TypeKitchen.FAST_FOOD, "08:00 - 22:00", ownerId);
		Restaurant created = Restaurant.builder().id(UUID.randomUUID()).name("Pizzaria").typeKitchen(TypeKitchen.FAST_FOOD).build();

		when(createRestaurantUseCase.execute(any(CreateRestaurantRequest.class))).thenReturn(created);

		mockMvc.perform(post("/v1/restaurant")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.success").value(true))
				.andExpect(jsonPath("$.data.name").value("Pizzaria"));
	}

	@Test
	void shouldReturn400WhenCreateRestaurantWithInvalidBody() throws Exception
	{
		mockMvc.perform(post("/v1/restaurant")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{}"))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.success").value(false));
	}

	@Test
	void shouldReturn412WhenRestaurantNameAlreadyExists() throws Exception
	{
		UUID ownerId = UUID.randomUUID();
		CreateRestaurantRequest request = new CreateRestaurantRequest("Pizzaria", "Rua A 123", TypeKitchen.FAST_FOOD, "08:00 - 22:00", ownerId);

		when(createRestaurantUseCase.execute(any(CreateRestaurantRequest.class)))
				.thenThrow(new RestaurantNameAlreadyExistsException("Name already exists"));

		mockMvc.perform(post("/v1/restaurant")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isPreconditionFailed())
				.andExpect(jsonPath("$.success").value(false));
	}

	// --- PUT /v1/restaurant/{id} ---

	@Test
	void shouldUpdateRestaurant() throws Exception
	{
		UUID id = UUID.randomUUID();
		UpdateRestaurantRequest request = new UpdateRestaurantRequest("Pizzaria Updated", "Rua B 456", TypeKitchen.HIGH_GASTRONOMY, "09:00 - 23:00");
		Restaurant updated = Restaurant.builder().id(id).name("Pizzaria Updated").typeKitchen(TypeKitchen.HIGH_GASTRONOMY).build();

		when(updateRestaurantUseCase.execute(eq(id), any(UpdateRestaurantRequest.class))).thenReturn(updated);

		mockMvc.perform(put("/v1/restaurant/{id}", id)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.success").value(true))
				.andExpect(jsonPath("$.data.name").value("Pizzaria Updated"));
	}

	@Test
	void shouldReturn412WhenUpdateRestaurantNotFound() throws Exception
	{
		UUID id = UUID.randomUUID();
		UpdateRestaurantRequest request = new UpdateRestaurantRequest("Pizzaria", "Rua A 123", TypeKitchen.FAST_FOOD, "08:00 - 22:00");

		when(updateRestaurantUseCase.execute(eq(id), any(UpdateRestaurantRequest.class)))
				.thenThrow(new RestaurantNotFoundException("Restaurant not found"));

		mockMvc.perform(put("/v1/restaurant/{id}", id)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isPreconditionFailed())
				.andExpect(jsonPath("$.success").value(false));
	}

	// --- DELETE /v1/restaurant/{id} ---

	@Test
	void shouldDeleteRestaurant() throws Exception
	{
		UUID id = UUID.randomUUID();
		doNothing().when(deleteRestaurantUseCase).execute(id);

		mockMvc.perform(delete("/v1/restaurant/{id}", id))
				.andExpect(status().isAccepted());

		verify(deleteRestaurantUseCase).execute(id);
	}
}
