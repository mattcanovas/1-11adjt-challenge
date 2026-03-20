package br.com.fiap.gfood.api.domain.model;

import br.com.fiap.gfood.api.domain.enums.TypeKitchen;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Restaurant {

    private UUID id;
    private String name;
    private String address;
    private TypeKitchen typeKitchen;
    private String openingHours;
    private User owner;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
