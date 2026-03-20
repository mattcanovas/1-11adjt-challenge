package br.com.fiap.gfood.api.application.controller;

import br.com.fiap.gfood.api.application.dto.ApiPageResponse;
import br.com.fiap.gfood.api.application.dto.ApiResponse;
import br.com.fiap.gfood.api.application.dto.CreateRestaurantRequest;
import br.com.fiap.gfood.api.application.dto.UpdateRestaurantRequest;
import br.com.fiap.gfood.api.application.usecase.CreateRestaurantUseCase;
import br.com.fiap.gfood.api.application.usecase.DeleteRestaurantUseCase;
import br.com.fiap.gfood.api.application.usecase.FindRestaurantsUseCase;
import br.com.fiap.gfood.api.application.usecase.UpdateRestaurantUseCase;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class RestaurantController implements RestaurantResource {

    private static final String CREATED_AT_SORT_PARAMETER = "createdAt";

    private final CreateRestaurantUseCase createRestaurantUseCase;
    private final FindRestaurantsUseCase findRestaurantsUseCase;
    private final UpdateRestaurantUseCase updateRestaurantUseCase;
    private final DeleteRestaurantUseCase deleteRestaurantUseCase;

    public RestaurantController(CreateRestaurantUseCase createRestaurantUseCase,
                                FindRestaurantsUseCase findRestaurantsUseCase,
                                UpdateRestaurantUseCase updateRestaurantUseCase,
                                DeleteRestaurantUseCase deleteRestaurantUseCase) {
        this.createRestaurantUseCase = createRestaurantUseCase;
        this.findRestaurantsUseCase = findRestaurantsUseCase;
        this.updateRestaurantUseCase = updateRestaurantUseCase;
        this.deleteRestaurantUseCase = deleteRestaurantUseCase;
    }

    @Override
    public ResponseEntity<ApiPageResponse> findAllFiltering(String name, Integer page, Integer size) {
        var pageable = PageRequest.of(page, size, Sort.by(CREATED_AT_SORT_PARAMETER));
        var result = findRestaurantsUseCase.execute(name, pageable);
        return ResponseEntity.ok(new ApiPageResponse(true, result));
    }

    @Override
    public ResponseEntity<ApiResponse> findById(UUID id) {
        var result = findRestaurantsUseCase.executeById(id);
        return ResponseEntity.ok(new ApiResponse(true, result));
    }

    @Override
    public ResponseEntity<ApiResponse> create(CreateRestaurantRequest payload) {
        var result = createRestaurantUseCase.execute(payload);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse(true, result));
    }

    @Override
    public ResponseEntity<ApiResponse> update(UUID id, UpdateRestaurantRequest payload) {
        var result = updateRestaurantUseCase.execute(id, payload);
        return ResponseEntity.ok(new ApiResponse(true, result));
    }

    @Override
    public ResponseEntity<Void> delete(UUID id) {
        deleteRestaurantUseCase.execute(id);
        return ResponseEntity.accepted().build();
    }
}
