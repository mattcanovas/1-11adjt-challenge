package br.com.fiap.gfood.api.data.gateway;

import br.com.fiap.gfood.api.data.entity.ItemEntity;
import br.com.fiap.gfood.api.data.entity.RestaurantEntity;
import br.com.fiap.gfood.api.data.entity.TypeUserEntity;
import br.com.fiap.gfood.api.data.entity.UserEntity;
import br.com.fiap.gfood.api.data.repository.ItemRepository;
import br.com.fiap.gfood.api.domain.enums.TypeKitchen;
import br.com.fiap.gfood.api.domain.model.Item;
import br.com.fiap.gfood.api.domain.model.Restaurant;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemGatewayImplTest {

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private ItemGatewayImpl itemGateway;

    @Test
    void shouldSaveItem() {
        var restaurant = Restaurant.builder()
                .id(UUID.randomUUID())
                .name("Burger King")
                .address("Av. Paulista, 1000")
                .typeKitchen(TypeKitchen.FAST_FOOD)
                .openingHours("08:00 - 22:00")
                .build();

        var item = Item.builder()
                .name("X-Burger")
                .description("Delicious burger")
                .price(new BigDecimal("29.90"))
                .availabilityToOrderLocal(true)
                .photo("/images/xburger.png")
                .restaurant(restaurant)
                .build();

        var savedEntity = buildItemEntity();
        when(itemRepository.save(any(ItemEntity.class))).thenReturn(savedEntity);

        var result = itemGateway.save(item);

        assertNotNull(result);
        assertEquals("X-Burger", result.getName());
        assertEquals(new BigDecimal("29.90"), result.getPrice());
        assertEquals(true, result.getAvailabilityToOrderLocal());
        assertNotNull(result.getRestaurant());
        verify(itemRepository).save(any(ItemEntity.class));
    }

    @Test
    void shouldFindAll() {
        var pageable = PageRequest.of(0, 10);
        var entity = buildItemEntity();
        var page = new PageImpl<>(List.of(entity));

        when(itemRepository.findAll(pageable)).thenReturn(page);

        var result = itemGateway.findAll(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(itemRepository).findAll(pageable);
    }

    @Test
    void shouldFindByNameContaining() {
        var pageable = PageRequest.of(0, 10);
        var entity = buildItemEntity();
        var page = new PageImpl<>(List.of(entity));

        when(itemRepository.findAllByNameContaining("Burger", pageable)).thenReturn(page);

        var result = itemGateway.findByNameContaining("Burger", pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(itemRepository).findAllByNameContaining("Burger", pageable);
    }

    @Test
    void shouldFindByIdWhenPresent() {
        var id = UUID.randomUUID();
        var entity = buildItemEntity();
        entity.setId(id);

        when(itemRepository.findById(id)).thenReturn(Optional.of(entity));

        var result = itemGateway.findById(id);

        assertTrue(result.isPresent());
        assertEquals(id, result.get().getId());
    }

    @Test
    void shouldReturnEmptyWhenNotFoundById() {
        var id = UUID.randomUUID();

        when(itemRepository.findById(id)).thenReturn(Optional.empty());

        var result = itemGateway.findById(id);

        assertFalse(result.isPresent());
    }

    @Test
    void shouldDeleteById() {
        var id = UUID.randomUUID();

        itemGateway.deleteById(id);

        verify(itemRepository).deleteById(id);
    }

    private ItemEntity buildItemEntity() {
        var typeUserEntity = new TypeUserEntity();
        typeUserEntity.setId(UUID.randomUUID());
        typeUserEntity.setName("OWNER");
        typeUserEntity.setDescription("Restaurant owner");
        typeUserEntity.setCreatedAt(LocalDateTime.now());
        typeUserEntity.setUpdatedAt(LocalDateTime.now());

        var ownerEntity = new UserEntity();
        ownerEntity.setId(UUID.randomUUID());
        ownerEntity.setFullName("John Doe");
        ownerEntity.setLogin("johndoe");
        ownerEntity.setPassword("password");
        ownerEntity.setEmail("john@test.com");
        ownerEntity.setAddress("Street 1");
        ownerEntity.setTypeUser(typeUserEntity);
        ownerEntity.setCreatedAt(LocalDateTime.now());
        ownerEntity.setUpdatedAt(LocalDateTime.now());

        var restaurantEntity = new RestaurantEntity();
        restaurantEntity.setId(UUID.randomUUID());
        restaurantEntity.setName("Burger King");
        restaurantEntity.setAddress("Av. Paulista, 1000");
        restaurantEntity.setTypeKitchen(TypeKitchen.FAST_FOOD);
        restaurantEntity.setOpeningHours("08:00 - 22:00");
        restaurantEntity.setOwner(ownerEntity);
        restaurantEntity.setCreatedAt(LocalDateTime.now());
        restaurantEntity.setUpdatedAt(LocalDateTime.now());

        var entity = new ItemEntity();
        entity.setId(UUID.randomUUID());
        entity.setName("X-Burger");
        entity.setDescription("Delicious burger");
        entity.setPrice(new BigDecimal("29.90"));
        entity.setAvailabilityToOrderLocal(true);
        entity.setPhoto("/images/xburger.png");
        entity.setRestaurant(restaurantEntity);
        entity.setCreatedAt(LocalDateTime.now());
        entity.setUpdatedAt(LocalDateTime.now());
        return entity;
    }
}
