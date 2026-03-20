package br.com.fiap.gfood.api.data.repository;

import br.com.fiap.gfood.api.data.entity.RestaurantEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RestaurantRepository extends JpaRepository<RestaurantEntity, UUID> {

    Page<RestaurantEntity> findAllByNameContaining(String name, Pageable pageable);

    boolean existsByName(String name);
}
