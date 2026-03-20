package br.com.fiap.gfood.api.application.usecase;

import br.com.fiap.gfood.api.application.dto.UpdateItemRequest;
import br.com.fiap.gfood.api.domain.exception.ItemNotFoundException;
import br.com.fiap.gfood.api.domain.gateway.ItemGateway;
import br.com.fiap.gfood.api.domain.model.Item;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
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
class UpdateItemUseCaseTest {

    @Mock
    private ItemGateway itemGateway;

    @InjectMocks
    private UpdateItemUseCase updateItemUseCase;

    @Test
    void shouldUpdateItemSuccessfully() {
        var id = UUID.randomUUID();
        var request = new UpdateItemRequest("Updated Burger", "New description", new BigDecimal("35.90"),
                false, "/images/updated.png");

        var existing = Item.builder()
                .id(id)
                .name("Old Burger")
                .description("Old description")
                .price(new BigDecimal("29.90"))
                .availabilityToOrderLocal(true)
                .photo("/images/old.png")
                .build();

        var updated = Item.builder()
                .id(id)
                .name(request.name())
                .description(request.description())
                .price(request.price())
                .availabilityToOrderLocal(request.availabilityToOrderLocal())
                .photo(request.photo())
                .build();

        when(itemGateway.findById(id)).thenReturn(Optional.of(existing));
        when(itemGateway.save(any(Item.class))).thenReturn(updated);

        var result = updateItemUseCase.execute(id, request);

        assertNotNull(result);
        assertEquals(request.name(), result.getName());
        assertEquals(request.description(), result.getDescription());
        assertEquals(request.price(), result.getPrice());
        assertEquals(request.availabilityToOrderLocal(), result.getAvailabilityToOrderLocal());
        assertEquals(request.photo(), result.getPhoto());
        verify(itemGateway).save(any(Item.class));
    }

    @Test
    void shouldThrowExceptionWhenItemNotFound() {
        var id = UUID.randomUUID();
        var request = new UpdateItemRequest("Name", "Desc", new BigDecimal("10.00"), true, null);

        when(itemGateway.findById(id)).thenReturn(Optional.empty());

        assertThrows(ItemNotFoundException.class, () -> updateItemUseCase.execute(id, request));
        verify(itemGateway, never()).save(any(Item.class));
    }

    @Test
    void shouldUpdateItemWithNullPhoto() {
        var id = UUID.randomUUID();
        var request = new UpdateItemRequest("Burger", "Desc", new BigDecimal("19.90"), true, null);

        var existing = Item.builder().id(id).name("Old").description("Old").price(new BigDecimal("10.00"))
                .availabilityToOrderLocal(false).photo("/images/old.png").build();

        var updated = Item.builder().id(id).name(request.name()).description(request.description())
                .price(request.price()).availabilityToOrderLocal(request.availabilityToOrderLocal())
                .photo(null).build();

        when(itemGateway.findById(id)).thenReturn(Optional.of(existing));
        when(itemGateway.save(any(Item.class))).thenReturn(updated);

        var result = updateItemUseCase.execute(id, request);

        assertNotNull(result);
        assertEquals(null, result.getPhoto());
        verify(itemGateway).save(any(Item.class));
    }
}
