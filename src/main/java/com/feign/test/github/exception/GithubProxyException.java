package com.feign.test.github.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
public class GithubProxyException extends RuntimeException {
    private final HttpStatus httpStatus;
    private final LocalDateTime localDateTime;

    public GithubProxyException(HttpStatus httpStatus, String message) {
        super(message);
        this.httpStatus = httpStatus;
        this.localDateTime = LocalDateTime.now();
    }
}
