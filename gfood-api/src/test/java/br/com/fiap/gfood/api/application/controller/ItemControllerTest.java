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

import java.math.BigDecimal;
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

import br.com.fiap.gfood.api.application.dto.CreateItemRequest;
import br.com.fiap.gfood.api.application.dto.UpdateItemRequest;
import br.com.fiap.gfood.api.application.usecase.CreateItemUseCase;
import br.com.fiap.gfood.api.application.usecase.DeleteItemUseCase;
import br.com.fiap.gfood.api.application.usecase.FindItemsUseCase;
import br.com.fiap.gfood.api.application.usecase.UpdateItemUseCase;
import br.com.fiap.gfood.api.domain.exception.ItemNotFoundException;
import br.com.fiap.gfood.api.domain.model.Item;

@WebMvcTest(ItemResource.class)
class ItemControllerTest
{
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private CreateItemUseCase createItemUseCase;

	@MockBean
	private FindItemsUseCase findItemsUseCase;

	@MockBean
	private UpdateItemUseCase updateItemUseCase;

	@MockBean
	private DeleteItemUseCase deleteItemUseCase;

	// --- GET /v1/item ---

	@Test
	void shouldFindAllItems() throws Exception
	{
		Item item = Item.builder().id(UUID.randomUUID()).name("Burger").price(BigDecimal.valueOf(25.90)).build();
		when(findItemsUseCase.execute(any(), any(Pageable.class)))
				.thenReturn(new PageImpl<>(List.of(item)));

		mockMvc.perform(get("/v1/item"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.success").value(true))
				.andExpect(jsonPath("$.page.content[0].name").value("Burger"));
	}

	@Test
	void shouldFindItemsWithNameFilter() throws Exception
	{
		Item item = Item.builder().id(UUID.randomUUID()).name("Burger").build();
		when(findItemsUseCase.execute(eq("Burg"), any(Pageable.class)))
				.thenReturn(new PageImpl<>(List.of(item)));

		mockMvc.perform(get("/v1/item").param("name", "Burg"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.page.content[0].name").value("Burger"));
	}

	// --- GET /v1/item/{id} ---

	@Test
	void shouldFindItemById() throws Exception
	{
		UUID id = UUID.randomUUID();
		Item item = Item.builder().id(id).name("Burger").price(BigDecimal.valueOf(25.90)).build();

		when(findItemsUseCase.executeById(id)).thenReturn(item);

		mockMvc.perform(get("/v1/item/{id}", id))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.success").value(true))
				.andExpect(jsonPath("$.data.name").value("Burger"));
	}

	@Test
	void shouldReturn412WhenItemNotFoundById() throws Exception
	{
		UUID id = UUID.randomUUID();

		when(findItemsUseCase.executeById(id))
				.thenThrow(new ItemNotFoundException("Item not found"));

		mockMvc.perform(get("/v1/item/{id}", id))
				.andExpect(status().isPreconditionFailed())
				.andExpect(jsonPath("$.success").value(false));
	}

	// --- POST /v1/item ---

	@Test
	void shouldCreateItem() throws Exception
	{
		UUID restaurantId = UUID.randomUUID();
		CreateItemRequest request = new CreateItemRequest("Burger", "Delicious burger", BigDecimal.valueOf(25.90), true, "photo.jpg", restaurantId);
		Item created = Item.builder().id(UUID.randomUUID()).name("Burger").price(BigDecimal.valueOf(25.90)).build();

		when(createItemUseCase.execute(any(CreateItemRequest.class))).thenReturn(created);

		mockMvc.perform(post("/v1/item")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.success").value(true))
				.andExpect(jsonPath("$.data.name").value("Burger"));
	}

	@Test
	void shouldReturn400WhenCreateItemWithInvalidBody() throws Exception
	{
		mockMvc.perform(post("/v1/item")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{}"))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.success").value(false));
	}

	// --- PUT /v1/item/{id} ---

	@Test
	void shouldUpdateItem() throws Exception
	{
		UUID id = UUID.randomUUID();
		UpdateItemRequest request = new UpdateItemRequest("Burger Updated", "Even better", BigDecimal.valueOf(29.90), true, "photo2.jpg");
		Item updated = Item.builder().id(id).name("Burger Updated").price(BigDecimal.valueOf(29.90)).build();

		when(updateItemUseCase.execute(eq(id), any(UpdateItemRequest.class))).thenReturn(updated);

		mockMvc.perform(put("/v1/item/{id}", id)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.success").value(true))
				.andExpect(jsonPath("$.data.name").value("Burger Updated"));
	}

	@Test
	void shouldReturn412WhenUpdateItemNotFound() throws Exception
	{
		UUID id = UUID.randomUUID();
		UpdateItemRequest request = new UpdateItemRequest("Burger", "Delicious", BigDecimal.valueOf(25.90), true, "photo.jpg");

		when(updateItemUseCase.execute(eq(id), any(UpdateItemRequest.class)))
				.thenThrow(new ItemNotFoundException("Item not found"));

		mockMvc.perform(put("/v1/item/{id}", id)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isPreconditionFailed())
				.andExpect(jsonPath("$.success").value(false));
	}

	// --- DELETE /v1/item/{id} ---

	@Test
	void shouldDeleteItem() throws Exception
	{
		UUID id = UUID.randomUUID();
		doNothing().when(deleteItemUseCase).execute(id);

		mockMvc.perform(delete("/v1/item/{id}", id))
				.andExpect(status().isAccepted());

		verify(deleteItemUseCase).execute(id);
	}
}
