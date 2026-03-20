package br.com.fiap.gfood.api.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Item {

    private UUID id;
    private String name;
    private String description;
    private BigDecimal price;
    private Boolean availabilityToOrderLocal;
    private String photo;
    private Restaurant restaurant;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
