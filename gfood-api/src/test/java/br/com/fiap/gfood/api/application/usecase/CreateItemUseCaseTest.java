package br.com.fiap.gfood.api.application.usecase;

import br.com.fiap.gfood.api.application.dto.CreateItemRequest;
import br.com.fiap.gfood.api.domain.enums.TypeKitchen;
import br.com.fiap.gfood.api.domain.exception.RestaurantNotFoundException;
import br.com.fiap.gfood.api.domain.gateway.ItemGateway;
import br.com.fiap.gfood.api.domain.gateway.RestaurantGateway;
import br.com.fiap.gfood.api.domain.model.Item;
import br.com.fiap.gfood.api.domain.model.Restaurant;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateItemUseCaseTest {

    @Mock
    private ItemGateway itemGateway;

    @Mock
    private RestaurantGateway restaurantGateway;

    @InjectMocks
    private CreateItemUseCase createItemUseCase;

    @Test
    void shouldCreateItemSuccessfully() {
        var restaurantId = UUID.randomUUID();
        var request = new CreateItemRequest("X-Burger", "Delicious burger", new BigDecimal("29.90"),
                true, "/images/xburger.png", restaurantId);

        var restaurant = Restaurant.builder()
                .id(restaurantId)
                .name("Burger King")
                .address("Av. Paulista, 1000")
                .typeKitchen(TypeKitchen.FAST_FOOD)
                .openingHours("08:00 - 22:00")
                .build();

        var savedItem = Item.builder()
                .id(UUID.randomUUID())
                .name(request.name())
                .description(request.description())
                .price(request.price())
                .availabilityToOrderLocal(request.availabilityToOrderLocal())
                .photo(request.photo())
                .restaurant(restaurant)
                .build();

        when(restaurantGateway.findById(restaurantId)).thenReturn(Optional.of(restaurant));
        when(itemGateway.save(any(Item.class))).thenReturn(savedItem);

        var result = createItemUseCase.execute(request);

        assertNotNull(result);
        assertEquals(request.name(), result.getName());
        assertEquals(request.description(), result.getDescription());
        assertEquals(request.price(), result.getPrice());
        assertEquals(request.availabilityToOrderLocal(), result.getAvailabilityToOrderLocal());
        assertEquals(request.photo(), result.getPhoto());
        assertNotNull(result.getRestaurant());
        verify(itemGateway).save(any(Item.class));
    }

    @Test
    void shouldCreateItemWithNullPhoto() {
        var restaurantId = UUID.randomUUID();
        var request = new CreateItemRequest("X-Burger", "Delicious burger", new BigDecimal("29.90"),
                false, null, restaurantId);

        var restaurant = Restaurant.builder().id(restaurantId).name("Burger King").build();

        var savedItem = Item.builder()
                .id(UUID.randomUUID())
                .name(request.name())
                .description(request.description())
                .price(request.price())
                .availabilityToOrderLocal(request.availabilityToOrderLocal())
                .photo(null)
                .restaurant(restaurant)
                .build();

        when(restaurantGateway.findById(restaurantId)).thenReturn(Optional.of(restaurant));
        when(itemGateway.save(any(Item.class))).thenReturn(savedItem);

        var result = createItemUseCase.execute(request);

        assertNotNull(result);
        assertEquals(false, result.getAvailabilityToOrderLocal());
        verify(itemGateway).save(any(Item.class));
    }

    @Test
    void shouldThrowExceptionWhenRestaurantNotFound() {
        var restaurantId = UUID.randomUUID();
        var request = new CreateItemRequest("X-Burger", "Delicious burger", new BigDecimal("29.90"),
                true, "/images/xburger.png", restaurantId);

        when(restaurantGateway.findById(restaurantId)).thenReturn(Optional.empty());

        assertThrows(RestaurantNotFoundException.class, () -> createItemUseCase.execute(request));
        verify(itemGateway, never()).save(any(Item.class));
    }
}
