package com.feign.test.github.exception;

import org.springframework.http.HttpStatus;

public class LocalRepositoryNotFoundException extends GithubProxyException {
    public LocalRepositoryNotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }
}
