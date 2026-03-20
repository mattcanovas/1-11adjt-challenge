package br.com.fiap.gfood.api.domain.exception;

import java.io.Serial;

public class RestaurantNotFoundException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public RestaurantNotFoundException(String message) {
        super(message);
    }
}
