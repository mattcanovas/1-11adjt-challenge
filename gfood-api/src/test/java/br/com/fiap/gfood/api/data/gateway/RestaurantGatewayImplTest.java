package br.com.fiap.gfood.api.data.gateway;

import br.com.fiap.gfood.api.data.entity.RestaurantEntity;
import br.com.fiap.gfood.api.data.entity.TypeUserEntity;
import br.com.fiap.gfood.api.data.entity.UserEntity;
import br.com.fiap.gfood.api.data.repository.RestaurantRepository;
import br.com.fiap.gfood.api.domain.enums.TypeKitchen;
import br.com.fiap.gfood.api.domain.model.Restaurant;
import br.com.fiap.gfood.api.domain.model.TypeUser;
import br.com.fiap.gfood.api.domain.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

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
class RestaurantGatewayImplTest {

    @Mock
    private RestaurantRepository restaurantRepository;

    @InjectMocks
    private RestaurantGatewayImpl restaurantGateway;

    @Test
    void shouldSaveRestaurant() {
        var ownerType = new TypeUser();
        ownerType.setId(UUID.randomUUID());
        ownerType.setName("OWNER");

        var owner = new User();
        owner.setId(UUID.randomUUID());
        owner.setFullName("John Doe");
        owner.setLogin("johndoe");
        owner.setPassword("password");
        owner.setEmail("john@test.com");
        owner.setAddress("Street 1");
        owner.setTypeUser(ownerType);

        var restaurant = Restaurant.builder()
                .name("Burger King")
                .address("Av. Paulista, 1000")
                .typeKitchen(TypeKitchen.FAST_FOOD)
                .openingHours("08:00 - 22:00")
                .owner(owner)
                .build();

        var savedEntity = buildRestaurantEntity();
        when(restaurantRepository.save(any(RestaurantEntity.class))).thenReturn(savedEntity);

        var result = restaurantGateway.save(restaurant);

        assertNotNull(result);
        assertEquals("Burger King", result.getName());
        assertEquals(TypeKitchen.FAST_FOOD, result.getTypeKitchen());
        assertNotNull(result.getOwner());
        verify(restaurantRepository).save(any(RestaurantEntity.class));
    }

    @Test
    void shouldFindAll() {
        var pageable = PageRequest.of(0, 10);
        var entity = buildRestaurantEntity();
        var page = new PageImpl<>(List.of(entity));

        when(restaurantRepository.findAll(pageable)).thenReturn(page);

        var result = restaurantGateway.findAll(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(restaurantRepository).findAll(pageable);
    }

    @Test
    void shouldFindByNameContaining() {
        var pageable = PageRequest.of(0, 10);
        var entity = buildRestaurantEntity();
        var page = new PageImpl<>(List.of(entity));

        when(restaurantRepository.findAllByNameContaining("Burger", pageable)).thenReturn(page);

        var result = restaurantGateway.findByNameContaining("Burger", pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(restaurantRepository).findAllByNameContaining("Burger", pageable);
    }

    @Test
    void shouldFindByIdWhenPresent() {
        var id = UUID.randomUUID();
        var entity = buildRestaurantEntity();
        entity.setId(id);

        when(restaurantRepository.findById(id)).thenReturn(Optional.of(entity));

        var result = restaurantGateway.findById(id);

        assertTrue(result.isPresent());
        assertEquals(id, result.get().getId());
    }

    @Test
    void shouldReturnEmptyWhenNotFoundById() {
        var id = UUID.randomUUID();

        when(restaurantRepository.findById(id)).thenReturn(Optional.empty());

        var result = restaurantGateway.findById(id);

        assertFalse(result.isPresent());
    }

    @Test
    void shouldDeleteById() {
        var id = UUID.randomUUID();

        restaurantGateway.deleteById(id);

        verify(restaurantRepository).deleteById(id);
    }

    @Test
    void shouldReturnTrueWhenNameExists() {
        when(restaurantRepository.existsByName("Burger King")).thenReturn(true);

        assertTrue(restaurantGateway.existsByName("Burger King"));
    }

    @Test
    void shouldReturnFalseWhenNameDoesNotExist() {
        when(restaurantRepository.existsByName("Unknown")).thenReturn(false);

        assertFalse(restaurantGateway.existsByName("Unknown"));
    }

    private RestaurantEntity buildRestaurantEntity() {
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

        var entity = new RestaurantEntity();
        entity.setId(UUID.randomUUID());
        entity.setName("Burger King");
        entity.setAddress("Av. Paulista, 1000");
        entity.setTypeKitchen(TypeKitchen.FAST_FOOD);
        entity.setOpeningHours("08:00 - 22:00");
        entity.setOwner(ownerEntity);
        entity.setCreatedAt(LocalDateTime.now());
        entity.setUpdatedAt(LocalDateTime.now());
        return entity;
    }
}
