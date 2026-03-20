package br.com.fiap.gfood.api.application.usecase;

import br.com.fiap.gfood.api.application.dto.CreateRestaurantRequest;
import br.com.fiap.gfood.api.domain.enums.TypeKitchen;
import br.com.fiap.gfood.api.domain.exception.OwnerUserRequiredException;
import br.com.fiap.gfood.api.domain.exception.RestaurantNameAlreadyExistsException;
import br.com.fiap.gfood.api.domain.exception.UserNotFoundException;
import br.com.fiap.gfood.api.domain.gateway.RestaurantGateway;
import br.com.fiap.gfood.api.domain.gateway.UserGateway;
import br.com.fiap.gfood.api.domain.model.Restaurant;
import br.com.fiap.gfood.api.domain.model.TypeUser;
import br.com.fiap.gfood.api.domain.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateRestaurantUseCaseTest {

    @Mock
    private RestaurantGateway restaurantGateway;

    @Mock
    private UserGateway userGateway;

    @InjectMocks
    private CreateRestaurantUseCase createRestaurantUseCase;

    @Test
    void shouldCreateRestaurantSuccessfully() {
        var ownerId = UUID.randomUUID();
        var request = new CreateRestaurantRequest("Burger King", "Av. Paulista, 1000", TypeKitchen.FAST_FOOD, "08:00 - 22:00", ownerId);

        var ownerType = new TypeUser();
        ownerType.setId(UUID.randomUUID());
        ownerType.setName("OWNER");

        var owner = new User();
        owner.setId(ownerId);
        owner.setFullName("John Doe");
        owner.setTypeUser(ownerType);

        var savedRestaurant = Restaurant.builder()
                .id(UUID.randomUUID())
                .name(request.name())
                .address(request.address())
                .typeKitchen(request.typeKitchen())
                .owner(owner)
                .build();

        when(restaurantGateway.existsByName(request.name())).thenReturn(false);
        when(userGateway.findById(ownerId)).thenReturn(Optional.of(owner));
        when(restaurantGateway.save(any(Restaurant.class))).thenReturn(savedRestaurant);

        var result = createRestaurantUseCase.execute(request);

        assertNotNull(result);
        assertEquals(request.name(), result.getName());
        assertEquals(request.address(), result.getAddress());
        assertEquals(request.typeKitchen(), result.getTypeKitchen());
        verify(restaurantGateway).save(any(Restaurant.class));
    }

    @Test
    void shouldThrowExceptionWhenNameAlreadyExists() {
        var request = new CreateRestaurantRequest("Burger King", "Av. Paulista, 1000", TypeKitchen.FAST_FOOD, "08:00 - 22:00", UUID.randomUUID());

        when(restaurantGateway.existsByName(request.name())).thenReturn(true);

        assertThrows(RestaurantNameAlreadyExistsException.class, () -> createRestaurantUseCase.execute(request));
        verify(restaurantGateway, never()).save(any(Restaurant.class));
    }

    @Test
    void shouldThrowExceptionWhenOwnerNotFound() {
        var ownerId = UUID.randomUUID();
        var request = new CreateRestaurantRequest("Burger King", "Av. Paulista, 1000", TypeKitchen.FAST_FOOD, "08:00 - 22:00", ownerId);

        when(restaurantGateway.existsByName(request.name())).thenReturn(false);
        when(userGateway.findById(ownerId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> createRestaurantUseCase.execute(request));
        verify(restaurantGateway, never()).save(any(Restaurant.class));
    }

    @Test
    void shouldThrowExceptionWhenUserIsNotOwnerType() {
        var ownerId = UUID.randomUUID();
        var request = new CreateRestaurantRequest("Burger King", "Av. Paulista, 1000", TypeKitchen.FAST_FOOD, "08:00 - 22:00", ownerId);

        var customerType = new TypeUser();
        customerType.setId(UUID.randomUUID());
        customerType.setName("CUSTOMER");

        var user = new User();
        user.setId(ownerId);
        user.setFullName("Jane Doe");
        user.setTypeUser(customerType);

        when(restaurantGateway.existsByName(request.name())).thenReturn(false);
        when(userGateway.findById(ownerId)).thenReturn(Optional.of(user));

        assertThrows(OwnerUserRequiredException.class, () -> createRestaurantUseCase.execute(request));
        verify(restaurantGateway, never()).save(any(Restaurant.class));
    }

    @Test
    void shouldThrowExceptionWhenUserHasNoTypeUser() {
        var ownerId = UUID.randomUUID();
        var request = new CreateRestaurantRequest("Burger King", "Av. Paulista, 1000", TypeKitchen.FAST_FOOD, "08:00 - 22:00", ownerId);

        var user = new User();
        user.setId(ownerId);
        user.setFullName("Jane Doe");
        user.setTypeUser(null);

        when(restaurantGateway.existsByName(request.name())).thenReturn(false);
        when(userGateway.findById(ownerId)).thenReturn(Optional.of(user));

        assertThrows(OwnerUserRequiredException.class, () -> createRestaurantUseCase.execute(request));
        verify(restaurantGateway, never()).save(any(Restaurant.class));
    }
}
