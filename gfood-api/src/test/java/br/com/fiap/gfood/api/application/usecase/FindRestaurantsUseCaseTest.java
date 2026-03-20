package br.com.fiap.gfood.api.application.usecase;

import br.com.fiap.gfood.api.domain.exception.RestaurantNotFoundException;
import br.com.fiap.gfood.api.domain.gateway.RestaurantGateway;
import br.com.fiap.gfood.api.domain.model.Restaurant;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FindRestaurantsUseCaseTest {

    @Mock
    private RestaurantGateway restaurantGateway;

    @InjectMocks
    private FindRestaurantsUseCase findRestaurantsUseCase;

    @Test
    void shouldFindAllWhenNameIsNull() {
        var pageable = PageRequest.of(0, 10);
        var restaurant = Restaurant.builder().id(UUID.randomUUID()).name("Burger King").build();
        var page = new PageImpl<>(List.of(restaurant));

        when(restaurantGateway.findAll(pageable)).thenReturn(page);

        var result = findRestaurantsUseCase.execute(null, pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(restaurantGateway).findAll(pageable);
    }

    @Test
    void shouldFindAllWhenNameIsBlank() {
        var pageable = PageRequest.of(0, 10);
        var page = new PageImpl<>(List.of(Restaurant.builder().id(UUID.randomUUID()).name("Test").build()));

        when(restaurantGateway.findAll(pageable)).thenReturn(page);

        var result = findRestaurantsUseCase.execute("  ", pageable);

        assertNotNull(result);
        verify(restaurantGateway).findAll(pageable);
    }

    @Test
    void shouldFindByNameContaining() {
        var pageable = PageRequest.of(0, 10);
        var restaurant = Restaurant.builder().id(UUID.randomUUID()).name("Burger King").build();
        var page = new PageImpl<>(List.of(restaurant));

        when(restaurantGateway.findByNameContaining("Burger", pageable)).thenReturn(page);

        var result = findRestaurantsUseCase.execute("Burger", pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(restaurantGateway).findByNameContaining("Burger", pageable);
    }

    @Test
    void shouldFindById() {
        var id = UUID.randomUUID();
        var restaurant = Restaurant.builder().id(id).name("Burger King").build();

        when(restaurantGateway.findById(id)).thenReturn(Optional.of(restaurant));

        var result = findRestaurantsUseCase.executeById(id);

        assertNotNull(result);
        assertEquals(id, result.getId());
        verify(restaurantGateway).findById(id);
    }

    @Test
    void shouldThrowExceptionWhenNotFoundById() {
        var id = UUID.randomUUID();

        when(restaurantGateway.findById(id)).thenReturn(Optional.empty());

        assertThrows(RestaurantNotFoundException.class, () -> findRestaurantsUseCase.executeById(id));
    }
}
