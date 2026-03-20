package br.com.fiap.gfood.api.application.usecase;

import br.com.fiap.gfood.api.domain.gateway.ItemGateway;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class DeleteItemUseCaseTest {

    @Mock
    private ItemGateway itemGateway;

    @InjectMocks
    private DeleteItemUseCase deleteItemUseCase;

    @Test
    void shouldDeleteItemAsynchronously() {
        var id = UUID.randomUUID();

        deleteItemUseCase.execute(id);

        verify(itemGateway, timeout(1000)).deleteById(id);
    }
}
