package br.com.fiap.gfood.api.application.usecase;

import br.com.fiap.gfood.api.domain.gateway.RestaurantGateway;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
public class DeleteRestaurantUseCase {

    private final RestaurantGateway restaurantGateway;

    public DeleteRestaurantUseCase(RestaurantGateway restaurantGateway) {
        this.restaurantGateway = restaurantGateway;
    }

    public void execute(UUID id) {
        CompletableFuture.runAsync(() -> restaurantGateway.deleteById(id));
    }
}
