package br.com.fiap.gfood.api.application.usecase;

import br.com.fiap.gfood.api.application.dto.UpdateItemRequest;
import br.com.fiap.gfood.api.domain.exception.ItemNotFoundException;
import br.com.fiap.gfood.api.domain.gateway.ItemGateway;
import br.com.fiap.gfood.api.domain.model.Item;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UpdateItemUseCase {

    private final ItemGateway itemGateway;

    public UpdateItemUseCase(ItemGateway itemGateway) {
        this.itemGateway = itemGateway;
    }

    public Item execute(UUID id, UpdateItemRequest request) {
        var item = itemGateway.findById(id)
                .orElseThrow(() -> new ItemNotFoundException("Item not found with id: " + id));

        item.setName(request.name());
        item.setDescription(request.description());
        item.setPrice(request.price());
        item.setAvailabilityToOrderLocal(request.availabilityToOrderLocal());
        item.setPhoto(request.photo());

        return itemGateway.save(item);
    }
}
