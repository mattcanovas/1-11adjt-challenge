package br.com.fiap.gfood.api.domain.exception;

import java.io.Serial;

public class OwnerUserRequiredException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public OwnerUserRequiredException(String message) {
        super(message);
    }
}
