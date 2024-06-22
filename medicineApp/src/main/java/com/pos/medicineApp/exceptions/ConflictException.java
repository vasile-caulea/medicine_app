package com.pos.medicineApp.exceptions;

public class ConflictException extends RuntimeException {
    public ConflictException(String mesage) {
        super(mesage);
    }
}
