package br.com.fiap.gfood.api.application.usecase;

import br.com.fiap.gfood.api.domain.exception.RestaurantNotFoundException;
import br.com.fiap.gfood.api.domain.gateway.RestaurantGateway;
import br.com.fiap.gfood.api.domain.model.Restaurant;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class FindRestaurantsUseCase {

    private final RestaurantGateway restaurantGateway;

    public FindRestaurantsUseCase(RestaurantGateway restaurantGateway) {
        this.restaurantGateway = restaurantGateway;
    }

    public Page<Restaurant> execute(String name, Pageable pageable) {
        if (StringUtils.isBlank(name)) {
            return restaurantGateway.findAll(pageable);
        }
        return restaurantGateway.findByNameContaining(name, pageable);
    }

    public Restaurant executeById(UUID id) {
        return restaurantGateway.findById(id)
                .orElseThrow(() -> new RestaurantNotFoundException("Restaurant not found with id: " + id));
    }
}
