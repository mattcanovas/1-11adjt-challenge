package br.com.fiap.gfood.api.data.repository;

import br.com.fiap.gfood.api.data.entity.ItemEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ItemRepository extends JpaRepository<ItemEntity, UUID> {

    Page<ItemEntity> findAllByNameContaining(String name, Pageable pageable);
}
