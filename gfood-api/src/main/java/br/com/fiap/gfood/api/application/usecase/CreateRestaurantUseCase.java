package br.com.fiap.gfood.api.application.usecase;

import br.com.fiap.gfood.api.application.dto.CreateRestaurantRequest;
import br.com.fiap.gfood.api.domain.exception.OwnerUserRequiredException;
import br.com.fiap.gfood.api.domain.exception.RestaurantNameAlreadyExistsException;
import br.com.fiap.gfood.api.domain.exception.UserNotFoundException;
import br.com.fiap.gfood.api.domain.gateway.RestaurantGateway;
import br.com.fiap.gfood.api.domain.gateway.UserGateway;
import br.com.fiap.gfood.api.domain.model.Restaurant;
import org.springframework.stereotype.Service;

@Service
public class CreateRestaurantUseCase {

    private static final String OWNER_TYPE_NAME = "OWNER";

    private final RestaurantGateway restaurantGateway;
    private final UserGateway userGateway;

    public CreateRestaurantUseCase(RestaurantGateway restaurantGateway, UserGateway userGateway) {
        this.restaurantGateway = restaurantGateway;
        this.userGateway = userGateway;
    }

    public Restaurant execute(CreateRestaurantRequest request) {
        if (restaurantGateway.existsByName(request.name())) {
            throw new RestaurantNameAlreadyExistsException("Restaurant name already exists: " + request.name());
        }

        var owner = userGateway.findById(request.ownerId())
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + request.ownerId()));

        if (owner.getTypeUser() == null || !OWNER_TYPE_NAME.equalsIgnoreCase(owner.getTypeUser().getName())) {
            throw new OwnerUserRequiredException("The user must have the type OWNER to create a restaurant.");
        }

        var restaurant = Restaurant.builder()
                .name(request.name())
                .address(request.address())
                .typeKitchen(request.typeKitchen())
                .openingHours(request.openingHours())
                .owner(owner)
                .build();

        return restaurantGateway.save(restaurant);
    }
}
