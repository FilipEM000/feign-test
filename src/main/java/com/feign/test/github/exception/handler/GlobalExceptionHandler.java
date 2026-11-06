package com.feign.test.github.exception.handler;

import com.feign.test.github.exception.GithubProxyException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(GithubProxyException.class)
    public ProblemDetail handleException(GithubProxyException exception) {
        log.warn("Error occurred: {}", exception.getMessage());
        return ProblemDetail.forStatusAndDetail(exception.getHttpStatus(), exception.getMessage());
    }
}
