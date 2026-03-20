package br.com.fiap.gfood.api.application.usecase;

import br.com.fiap.gfood.api.domain.exception.ItemNotFoundException;
import br.com.fiap.gfood.api.domain.gateway.ItemGateway;
import br.com.fiap.gfood.api.domain.model.Item;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FindItemsUseCaseTest {

    @Mock
    private ItemGateway itemGateway;

    @InjectMocks
    private FindItemsUseCase findItemsUseCase;

    @Test
    void shouldFindAllWhenNameIsNull() {
        var pageable = PageRequest.of(0, 10);
        var item = Item.builder().id(UUID.randomUUID()).name("X-Burger").price(new BigDecimal("29.90")).build();
        var page = new PageImpl<>(List.of(item));

        when(itemGateway.findAll(pageable)).thenReturn(page);

        var result = findItemsUseCase.execute(null, pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(itemGateway).findAll(pageable);
    }

    @Test
    void shouldFindAllWhenNameIsBlank() {
        var pageable = PageRequest.of(0, 10);
        var page = new PageImpl<>(List.of(Item.builder().id(UUID.randomUUID()).name("Test").build()));

        when(itemGateway.findAll(pageable)).thenReturn(page);

        var result = findItemsUseCase.execute("  ", pageable);

        assertNotNull(result);
        verify(itemGateway).findAll(pageable);
    }

    @Test
    void shouldFindByNameContaining() {
        var pageable = PageRequest.of(0, 10);
        var item = Item.builder().id(UUID.randomUUID()).name("X-Burger").build();
        var page = new PageImpl<>(List.of(item));

        when(itemGateway.findByNameContaining("Burger", pageable)).thenReturn(page);

        var result = findItemsUseCase.execute("Burger", pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(itemGateway).findByNameContaining("Burger", pageable);
    }

    @Test
    void shouldFindById() {
        var id = UUID.randomUUID();
        var item = Item.builder().id(id).name("X-Burger").build();

        when(itemGateway.findById(id)).thenReturn(Optional.of(item));

        var result = findItemsUseCase.executeById(id);

        assertNotNull(result);
        assertEquals(id, result.getId());
        verify(itemGateway).findById(id);
    }

    @Test
    void shouldThrowExceptionWhenNotFoundById() {
        var id = UUID.randomUUID();

        when(itemGateway.findById(id)).thenReturn(Optional.empty());

        assertThrows(ItemNotFoundException.class, () -> findItemsUseCase.executeById(id));
    }
}
