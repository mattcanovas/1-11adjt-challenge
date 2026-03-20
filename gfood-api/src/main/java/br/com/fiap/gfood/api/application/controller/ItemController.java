package br.com.fiap.gfood.api.application.controller;

import br.com.fiap.gfood.api.application.dto.ApiPageResponse;
import br.com.fiap.gfood.api.application.dto.ApiResponse;
import br.com.fiap.gfood.api.application.dto.CreateItemRequest;
import br.com.fiap.gfood.api.application.dto.UpdateItemRequest;
import br.com.fiap.gfood.api.application.usecase.CreateItemUseCase;
import br.com.fiap.gfood.api.application.usecase.DeleteItemUseCase;
import br.com.fiap.gfood.api.application.usecase.FindItemsUseCase;
import br.com.fiap.gfood.api.application.usecase.UpdateItemUseCase;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ItemController implements ItemResource {

    private static final String CREATED_AT_SORT_PARAMETER = "createdAt";

    private final CreateItemUseCase createItemUseCase;
    private final FindItemsUseCase findItemsUseCase;
    private final UpdateItemUseCase updateItemUseCase;
    private final DeleteItemUseCase deleteItemUseCase;

    public ItemController(CreateItemUseCase createItemUseCase,
                          FindItemsUseCase findItemsUseCase,
                          UpdateItemUseCase updateItemUseCase,
                          DeleteItemUseCase deleteItemUseCase) {
        this.createItemUseCase = createItemUseCase;
        this.findItemsUseCase = findItemsUseCase;
        this.updateItemUseCase = updateItemUseCase;
        this.deleteItemUseCase = deleteItemUseCase;
    }

    @Override
    public ResponseEntity<ApiPageResponse> findAllFiltering(String name, Integer page, Integer size) {
        var pageable = PageRequest.of(page, size, Sort.by(CREATED_AT_SORT_PARAMETER));
        var result = findItemsUseCase.execute(name, pageable);
        return ResponseEntity.ok(new ApiPageResponse(true, result));
    }

    @Override
    public ResponseEntity<ApiResponse> findById(UUID id) {
        var result = findItemsUseCase.executeById(id);
        return ResponseEntity.ok(new ApiResponse(true, result));
    }

    @Override
    public ResponseEntity<ApiResponse> create(CreateItemRequest payload) {
        var result = createItemUseCase.execute(payload);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse(true, result));
    }

    @Override
    public ResponseEntity<ApiResponse> update(UUID id, UpdateItemRequest payload) {
        var result = updateItemUseCase.execute(id, payload);
        return ResponseEntity.ok(new ApiResponse(true, result));
    }

    @Override
    public ResponseEntity<Void> delete(UUID id) {
        deleteItemUseCase.execute(id);
        return ResponseEntity.accepted().build();
    }
}
