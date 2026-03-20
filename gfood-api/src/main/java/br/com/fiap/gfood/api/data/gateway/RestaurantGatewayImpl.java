package br.com.fiap.gfood.api.data.gateway;

import br.com.fiap.gfood.api.data.entity.RestaurantEntity;
import br.com.fiap.gfood.api.data.entity.TypeUserEntity;
import br.com.fiap.gfood.api.data.entity.UserEntity;
import br.com.fiap.gfood.api.data.repository.RestaurantRepository;
import br.com.fiap.gfood.api.domain.gateway.RestaurantGateway;
import br.com.fiap.gfood.api.domain.model.Restaurant;
import br.com.fiap.gfood.api.domain.model.TypeUser;
import br.com.fiap.gfood.api.domain.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class RestaurantGatewayImpl implements RestaurantGateway {

    private final RestaurantRepository restaurantRepository;

    public RestaurantGatewayImpl(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    @Override
    public Page<Restaurant> findAll(Pageable pageable) {
        return restaurantRepository.findAll(pageable).map(this::toDomain);
    }

    @Override
    public Page<Restaurant> findByNameContaining(String name, Pageable pageable) {
        return restaurantRepository.findAllByNameContaining(name, pageable).map(this::toDomain);
    }

    @Override
    public Restaurant save(Restaurant restaurant) {
        var entity = toEntity(restaurant);
        var saved = restaurantRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<Restaurant> findById(UUID id) {
        return restaurantRepository.findById(id).map(this::toDomain);
    }

    @Override
    public void deleteById(UUID id) {
        restaurantRepository.deleteById(id);
    }

    @Override
    public boolean existsByName(String name) {
        return restaurantRepository.existsByName(name);
    }

    private Restaurant toDomain(RestaurantEntity entity) {
        var restaurant = new Restaurant();
        restaurant.setId(entity.getId());
        restaurant.setName(entity.getName());
        restaurant.setAddress(entity.getAddress());
        restaurant.setTypeKitchen(entity.getTypeKitchen());
        restaurant.setOpeningHours(entity.getOpeningHours());
        restaurant.setCreatedAt(entity.getCreatedAt());
        restaurant.setUpdatedAt(entity.getUpdatedAt());

        if (entity.getOwner() != null) {
            var owner = new User();
            owner.setId(entity.getOwner().getId());
            owner.setFullName(entity.getOwner().getFullName());
            owner.setLogin(entity.getOwner().getLogin());
            owner.setPassword(entity.getOwner().getPassword());
            owner.setEmail(entity.getOwner().getEmail());
            owner.setAddress(entity.getOwner().getAddress());
            owner.setCreatedAt(entity.getOwner().getCreatedAt());
            owner.setUpdatedAt(entity.getOwner().getUpdatedAt());

            if (entity.getOwner().getTypeUser() != null) {
                var typeUser = new TypeUser();
                typeUser.setId(entity.getOwner().getTypeUser().getId());
                typeUser.setName(entity.getOwner().getTypeUser().getName());
                typeUser.setDescription(entity.getOwner().getTypeUser().getDescription());
                typeUser.setCreatedAt(entity.getOwner().getTypeUser().getCreatedAt());
                typeUser.setUpdatedAt(entity.getOwner().getTypeUser().getUpdatedAt());
                owner.setTypeUser(typeUser);
            }

            restaurant.setOwner(owner);
        }

        return restaurant;
    }

    private RestaurantEntity toEntity(Restaurant restaurant) {
        var entity = new RestaurantEntity();
        entity.setId(restaurant.getId());
        entity.setName(restaurant.getName());
        entity.setAddress(restaurant.getAddress());
        entity.setTypeKitchen(restaurant.getTypeKitchen());
        entity.setOpeningHours(restaurant.getOpeningHours());
        entity.setCreatedAt(restaurant.getCreatedAt());
        entity.setUpdatedAt(restaurant.getUpdatedAt());

        if (restaurant.getOwner() != null) {
            var ownerEntity = new UserEntity();
            ownerEntity.setId(restaurant.getOwner().getId());
            ownerEntity.setFullName(restaurant.getOwner().getFullName());
            ownerEntity.setLogin(restaurant.getOwner().getLogin());
            ownerEntity.setPassword(restaurant.getOwner().getPassword());
            ownerEntity.setEmail(restaurant.getOwner().getEmail());
            ownerEntity.setAddress(restaurant.getOwner().getAddress());
            ownerEntity.setCreatedAt(restaurant.getOwner().getCreatedAt());
            ownerEntity.setUpdatedAt(restaurant.getOwner().getUpdatedAt());

            if (restaurant.getOwner().getTypeUser() != null) {
                var typeUserEntity = new TypeUserEntity();
                typeUserEntity.setId(restaurant.getOwner().getTypeUser().getId());
                typeUserEntity.setName(restaurant.getOwner().getTypeUser().getName());
                typeUserEntity.setDescription(restaurant.getOwner().getTypeUser().getDescription());
                typeUserEntity.setCreatedAt(restaurant.getOwner().getTypeUser().getCreatedAt());
                typeUserEntity.setUpdatedAt(restaurant.getOwner().getTypeUser().getUpdatedAt());
                ownerEntity.setTypeUser(typeUserEntity);
            }

            entity.setOwner(ownerEntity);
        }

        return entity;
    }
}
