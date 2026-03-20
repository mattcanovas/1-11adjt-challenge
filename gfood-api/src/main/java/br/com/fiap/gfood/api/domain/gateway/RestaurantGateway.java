package br.com.fiap.gfood.api.domain.gateway;

import br.com.fiap.gfood.api.domain.model.Restaurant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface RestaurantGateway {

    Page<Restaurant> findAll(Pageable pageable);

    Page<Restaurant> findByNameContaining(String name, Pageable pageable);

    Restaurant save(Restaurant restaurant);

    Optional<Restaurant> findById(UUID id);

    void deleteById(UUID id);

    boolean existsByName(String name);
}
