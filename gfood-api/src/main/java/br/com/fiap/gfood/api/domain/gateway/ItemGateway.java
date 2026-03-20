package br.com.fiap.gfood.api.domain.gateway;

import br.com.fiap.gfood.api.domain.model.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface ItemGateway {

    Page<Item> findAll(Pageable pageable);

    Page<Item> findByNameContaining(String name, Pageable pageable);

    Item save(Item item);

    Optional<Item> findById(UUID id);

    void deleteById(UUID id);
}
