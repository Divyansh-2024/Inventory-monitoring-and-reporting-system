package com.inventory.exceptions;

public class DuplicateUserException extends Exception {
    public DuplicateUserException() {
        super("User already exists!");
    }

    public DuplicateUserException(String message) {
        super(message);
    }
}
