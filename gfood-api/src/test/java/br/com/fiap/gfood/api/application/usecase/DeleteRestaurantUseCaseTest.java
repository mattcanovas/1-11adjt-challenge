package br.com.fiap.gfood.api.application.usecase;

import br.com.fiap.gfood.api.domain.gateway.RestaurantGateway;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class DeleteRestaurantUseCaseTest {

    @Mock
    private RestaurantGateway restaurantGateway;

    @InjectMocks
    private DeleteRestaurantUseCase deleteRestaurantUseCase;

    @Test
    void shouldDeleteRestaurantAsynchronously() {
        var id = UUID.randomUUID();

        deleteRestaurantUseCase.execute(id);

        verify(restaurantGateway, timeout(1000)).deleteById(id);
    }
}
