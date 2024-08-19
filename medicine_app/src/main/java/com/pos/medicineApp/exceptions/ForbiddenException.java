package com.pos.medicineApp.exceptions;

public class ForbiddenException extends RuntimeException {
    public ForbiddenException() {
        super("Forbidden!");
    }

    public ForbiddenException(String message) {
        super(message);
    }
}
