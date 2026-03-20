package br.com.fiap.gfood.api.application.usecase;

import br.com.fiap.gfood.api.application.dto.UpdateRestaurantRequest;
import br.com.fiap.gfood.api.domain.exception.RestaurantNameAlreadyExistsException;
import br.com.fiap.gfood.api.domain.exception.RestaurantNotFoundException;
import br.com.fiap.gfood.api.domain.gateway.RestaurantGateway;
import br.com.fiap.gfood.api.domain.model.Restaurant;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UpdateRestaurantUseCase {

    private final RestaurantGateway restaurantGateway;

    public UpdateRestaurantUseCase(RestaurantGateway restaurantGateway) {
        this.restaurantGateway = restaurantGateway;
    }

    public Restaurant execute(UUID id, UpdateRestaurantRequest request) {
        var restaurant = restaurantGateway.findById(id)
                .orElseThrow(() -> new RestaurantNotFoundException("Restaurant not found with id: " + id));

        if (!restaurant.getName().equals(request.name()) && restaurantGateway.existsByName(request.name())) {
            throw new RestaurantNameAlreadyExistsException("Restaurant name already exists: " + request.name());
        }

        restaurant.setName(request.name());
        restaurant.setAddress(request.address());
        restaurant.setTypeKitchen(request.typeKitchen());
        restaurant.setOpeningHours(request.openingHours());

        return restaurantGateway.save(restaurant);
    }
}
