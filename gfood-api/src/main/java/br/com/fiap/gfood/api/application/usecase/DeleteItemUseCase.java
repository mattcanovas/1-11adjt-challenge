package br.com.fiap.gfood.api.application.usecase;

import br.com.fiap.gfood.api.domain.gateway.ItemGateway;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
public class DeleteItemUseCase {

    private final ItemGateway itemGateway;

    public DeleteItemUseCase(ItemGateway itemGateway) {
        this.itemGateway = itemGateway;
    }

    public void execute(UUID id) {
        CompletableFuture.runAsync(() -> itemGateway.deleteById(id));
    }
}
