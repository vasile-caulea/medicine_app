package com.pos.medicineApp.exceptions;

public class UnauthorizedException extends RuntimeException {

    public UnauthorizedException() {
        super();
    }
    public UnauthorizedException(String message) {
        super(message);
    }
}
