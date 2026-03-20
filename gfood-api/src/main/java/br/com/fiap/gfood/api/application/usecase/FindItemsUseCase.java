package br.com.fiap.gfood.api.application.usecase;

import br.com.fiap.gfood.api.domain.exception.ItemNotFoundException;
import br.com.fiap.gfood.api.domain.gateway.ItemGateway;
import br.com.fiap.gfood.api.domain.model.Item;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class FindItemsUseCase {

    private final ItemGateway itemGateway;

    public FindItemsUseCase(ItemGateway itemGateway) {
        this.itemGateway = itemGateway;
    }

    public Page<Item> execute(String name, Pageable pageable) {
        if (StringUtils.isBlank(name)) {
            return itemGateway.findAll(pageable);
        }
        return itemGateway.findByNameContaining(name, pageable);
    }

    public Item executeById(UUID id) {
        return itemGateway.findById(id)
                .orElseThrow(() -> new ItemNotFoundException("Item not found with id: " + id));
    }
}
