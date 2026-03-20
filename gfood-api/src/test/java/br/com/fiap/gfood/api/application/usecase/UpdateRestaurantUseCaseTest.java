package br.com.fiap.gfood.api.application.usecase;

import br.com.fiap.gfood.api.application.dto.UpdateRestaurantRequest;
import br.com.fiap.gfood.api.domain.enums.TypeKitchen;
import br.com.fiap.gfood.api.domain.exception.RestaurantNameAlreadyExistsException;
import br.com.fiap.gfood.api.domain.exception.RestaurantNotFoundException;
import br.com.fiap.gfood.api.domain.gateway.RestaurantGateway;
import br.com.fiap.gfood.api.domain.model.Restaurant;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
class UpdateRestaurantUseCaseTest {

    @Mock
    private RestaurantGateway restaurantGateway;

    @InjectMocks
    private UpdateRestaurantUseCase updateRestaurantUseCase;

    @Test
    void shouldUpdateRestaurantSuccessfully() {
        var id = UUID.randomUUID();
        var request = new UpdateRestaurantRequest("Updated Name", "New Address, 200", TypeKitchen.HIGH_GASTRONOMY, "10:00 - 23:00");
        var existing = Restaurant.builder().id(id).name("Old Name").address("Old Address").typeKitchen(TypeKitchen.FAST_FOOD).build();
        var updated = Restaurant.builder().id(id).name(request.name()).address(request.address()).typeKitchen(request.typeKitchen()).build();

        when(restaurantGateway.findById(id)).thenReturn(Optional.of(existing));
        when(restaurantGateway.existsByName(request.name())).thenReturn(false);
        when(restaurantGateway.save(any(Restaurant.class))).thenReturn(updated);

        var result = updateRestaurantUseCase.execute(id, request);

        assertNotNull(result);
        assertEquals(request.name(), result.getName());
        assertEquals(request.address(), result.getAddress());
        assertEquals(request.typeKitchen(), result.getTypeKitchen());
        verify(restaurantGateway).save(any(Restaurant.class));
    }

    @Test
    void shouldThrowExceptionWhenRestaurantNotFound() {
        var id = UUID.randomUUID();
        var request = new UpdateRestaurantRequest("Name", "Address, 100", TypeKitchen.FAST_FOOD, "09:00 - 21:00");

        when(restaurantGateway.findById(id)).thenReturn(Optional.empty());

        assertThrows(RestaurantNotFoundException.class, () -> updateRestaurantUseCase.execute(id, request));
        verify(restaurantGateway, never()).save(any(Restaurant.class));
    }

    @Test
    void shouldThrowExceptionWhenNewNameAlreadyExists() {
        var id = UUID.randomUUID();
        var request = new UpdateRestaurantRequest("Taken Name", "Address, 100", TypeKitchen.FAST_FOOD, "09:00 - 21:00");
        var existing = Restaurant.builder().id(id).name("Original Name").address("Address").typeKitchen(TypeKitchen.FAST_FOOD).build();

        when(restaurantGateway.findById(id)).thenReturn(Optional.of(existing));
        when(restaurantGateway.existsByName(request.name())).thenReturn(true);

        assertThrows(RestaurantNameAlreadyExistsException.class, () -> updateRestaurantUseCase.execute(id, request));
        verify(restaurantGateway, never()).save(any(Restaurant.class));
    }

    @Test
    void shouldSkipNameCheckWhenNameUnchanged() {
        var id = UUID.randomUUID();
        var request = new UpdateRestaurantRequest("Same Name", "New Address, 300", TypeKitchen.FOOD_TRUCK, "11:00 - 20:00");
        var existing = Restaurant.builder().id(id).name("Same Name").address("Old Address").typeKitchen(TypeKitchen.FAST_FOOD).build();
        var updated = Restaurant.builder().id(id).name(request.name()).address(request.address()).typeKitchen(request.typeKitchen()).build();

        when(restaurantGateway.findById(id)).thenReturn(Optional.of(existing));
        when(restaurantGateway.save(any(Restaurant.class))).thenReturn(updated);

        var result = updateRestaurantUseCase.execute(id, request);

        assertNotNull(result);
        assertEquals(request.address(), result.getAddress());
        verify(restaurantGateway, never()).existsByName(any());
        verify(restaurantGateway).save(any(Restaurant.class));
    }
}
