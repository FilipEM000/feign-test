package com.feing.test.github.exception;

public class LocalRepositoryNotFoundException extends RuntimeException {
    public LocalRepositoryNotFoundException(String message) {
        super(message);
    }
}
