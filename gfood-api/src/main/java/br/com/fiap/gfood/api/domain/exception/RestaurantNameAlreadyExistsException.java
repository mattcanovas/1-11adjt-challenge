package br.com.fiap.gfood.api.domain.exception;

import java.io.Serial;

public class RestaurantNameAlreadyExistsException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public RestaurantNameAlreadyExistsException(String message) {
        super(message);
    }
}
