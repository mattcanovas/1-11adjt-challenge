package br.com.fiap.gfood.api.application.usecase;

import br.com.fiap.gfood.api.application.dto.CreateItemRequest;
import br.com.fiap.gfood.api.domain.exception.RestaurantNotFoundException;
import br.com.fiap.gfood.api.domain.gateway.ItemGateway;
import br.com.fiap.gfood.api.domain.gateway.RestaurantGateway;
import br.com.fiap.gfood.api.domain.model.Item;
import org.springframework.stereotype.Service;

@Service
public class CreateItemUseCase {

    private final ItemGateway itemGateway;
    private final RestaurantGateway restaurantGateway;

    public CreateItemUseCase(ItemGateway itemGateway, RestaurantGateway restaurantGateway) {
        this.itemGateway = itemGateway;
        this.restaurantGateway = restaurantGateway;
    }

    public Item execute(CreateItemRequest request) {
        var restaurant = restaurantGateway.findById(request.restaurantId())
                .orElseThrow(() -> new RestaurantNotFoundException("Restaurant not found with id: " + request.restaurantId()));

        var item = Item.builder()
                .name(request.name())
                .description(request.description())
                .price(request.price())
                .availabilityToOrderLocal(request.availabilityToOrderLocal())
                .photo(request.photo())
                .restaurant(restaurant)
                .build();

        return itemGateway.save(item);
    }
}
