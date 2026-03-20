package br.com.fiap.gfood.api.data.gateway;

import br.com.fiap.gfood.api.data.entity.ItemEntity;
import br.com.fiap.gfood.api.data.entity.RestaurantEntity;
import br.com.fiap.gfood.api.data.entity.TypeUserEntity;
import br.com.fiap.gfood.api.data.entity.UserEntity;
import br.com.fiap.gfood.api.data.repository.ItemRepository;
import br.com.fiap.gfood.api.domain.gateway.ItemGateway;
import br.com.fiap.gfood.api.domain.model.Item;
import br.com.fiap.gfood.api.domain.model.Restaurant;
import br.com.fiap.gfood.api.domain.model.TypeUser;
import br.com.fiap.gfood.api.domain.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class ItemGatewayImpl implements ItemGateway {

    private final ItemRepository itemRepository;

    public ItemGatewayImpl(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @Override
    public Page<Item> findAll(Pageable pageable) {
        return itemRepository.findAll(pageable).map(this::toDomain);
    }

    @Override
    public Page<Item> findByNameContaining(String name, Pageable pageable) {
        return itemRepository.findAllByNameContaining(name, pageable).map(this::toDomain);
    }

    @Override
    public Item save(Item item) {
        var entity = toEntity(item);
        var saved = itemRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<Item> findById(UUID id) {
        return itemRepository.findById(id).map(this::toDomain);
    }

    @Override
    public void deleteById(UUID id) {
        itemRepository.deleteById(id);
    }

    private Item toDomain(ItemEntity entity) {
        var item = new Item();
        item.setId(entity.getId());
        item.setName(entity.getName());
        item.setDescription(entity.getDescription());
        item.setPrice(entity.getPrice());
        item.setAvailabilityToOrderLocal(entity.getAvailabilityToOrderLocal());
        item.setPhoto(entity.getPhoto());
        item.setCreatedAt(entity.getCreatedAt());
        item.setUpdatedAt(entity.getUpdatedAt());

        if (entity.getRestaurant() != null) {
            var restaurant = new Restaurant();
            restaurant.setId(entity.getRestaurant().getId());
            restaurant.setName(entity.getRestaurant().getName());
            restaurant.setAddress(entity.getRestaurant().getAddress());
            restaurant.setTypeKitchen(entity.getRestaurant().getTypeKitchen());
            restaurant.setOpeningHours(entity.getRestaurant().getOpeningHours());
            restaurant.setCreatedAt(entity.getRestaurant().getCreatedAt());
            restaurant.setUpdatedAt(entity.getRestaurant().getUpdatedAt());

            if (entity.getRestaurant().getOwner() != null) {
                var owner = new User();
                owner.setId(entity.getRestaurant().getOwner().getId());
                owner.setFullName(entity.getRestaurant().getOwner().getFullName());
                owner.setLogin(entity.getRestaurant().getOwner().getLogin());
                owner.setPassword(entity.getRestaurant().getOwner().getPassword());
                owner.setEmail(entity.getRestaurant().getOwner().getEmail());
                owner.setAddress(entity.getRestaurant().getOwner().getAddress());
                owner.setCreatedAt(entity.getRestaurant().getOwner().getCreatedAt());
                owner.setUpdatedAt(entity.getRestaurant().getOwner().getUpdatedAt());

                if (entity.getRestaurant().getOwner().getTypeUser() != null) {
                    var typeUser = new TypeUser();
                    typeUser.setId(entity.getRestaurant().getOwner().getTypeUser().getId());
                    typeUser.setName(entity.getRestaurant().getOwner().getTypeUser().getName());
                    typeUser.setDescription(entity.getRestaurant().getOwner().getTypeUser().getDescription());
                    typeUser.setCreatedAt(entity.getRestaurant().getOwner().getTypeUser().getCreatedAt());
                    typeUser.setUpdatedAt(entity.getRestaurant().getOwner().getTypeUser().getUpdatedAt());
                    owner.setTypeUser(typeUser);
                }

                restaurant.setOwner(owner);
            }

            item.setRestaurant(restaurant);
        }

        return item;
    }

    private ItemEntity toEntity(Item item) {
        var entity = new ItemEntity();
        entity.setId(item.getId());
        entity.setName(item.getName());
        entity.setDescription(item.getDescription());
        entity.setPrice(item.getPrice());
        entity.setAvailabilityToOrderLocal(item.getAvailabilityToOrderLocal());
        entity.setPhoto(item.getPhoto());
        entity.setCreatedAt(item.getCreatedAt());
        entity.setUpdatedAt(item.getUpdatedAt());

        if (item.getRestaurant() != null) {
            var restaurantEntity = new RestaurantEntity();
            restaurantEntity.setId(item.getRestaurant().getId());
            restaurantEntity.setName(item.getRestaurant().getName());
            restaurantEntity.setAddress(item.getRestaurant().getAddress());
            restaurantEntity.setTypeKitchen(item.getRestaurant().getTypeKitchen());
            restaurantEntity.setOpeningHours(item.getRestaurant().getOpeningHours());
            restaurantEntity.setCreatedAt(item.getRestaurant().getCreatedAt());
            restaurantEntity.setUpdatedAt(item.getRestaurant().getUpdatedAt());

            if (item.getRestaurant().getOwner() != null) {
                var ownerEntity = new UserEntity();
                ownerEntity.setId(item.getRestaurant().getOwner().getId());
                ownerEntity.setFullName(item.getRestaurant().getOwner().getFullName());
                ownerEntity.setLogin(item.getRestaurant().getOwner().getLogin());
                ownerEntity.setPassword(item.getRestaurant().getOwner().getPassword());
                ownerEntity.setEmail(item.getRestaurant().getOwner().getEmail());
                ownerEntity.setAddress(item.getRestaurant().getOwner().getAddress());
                ownerEntity.setCreatedAt(item.getRestaurant().getOwner().getCreatedAt());
                ownerEntity.setUpdatedAt(item.getRestaurant().getOwner().getUpdatedAt());

                if (item.getRestaurant().getOwner().getTypeUser() != null) {
                    var typeUserEntity = new TypeUserEntity();
                    typeUserEntity.setId(item.getRestaurant().getOwner().getTypeUser().getId());
                    typeUserEntity.setName(item.getRestaurant().getOwner().getTypeUser().getName());
                    typeUserEntity.setDescription(item.getRestaurant().getOwner().getTypeUser().getDescription());
                    typeUserEntity.setCreatedAt(item.getRestaurant().getOwner().getTypeUser().getCreatedAt());
                    typeUserEntity.setUpdatedAt(item.getRestaurant().getOwner().getTypeUser().getUpdatedAt());
                    ownerEntity.setTypeUser(typeUserEntity);
                }

                restaurantEntity.setOwner(ownerEntity);
            }

            entity.setRestaurant(restaurantEntity);
        }

        return entity;
    }
}
