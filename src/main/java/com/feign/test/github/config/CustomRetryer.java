package com.feign.test.github.config;

import feign.RetryableException;
import feign.Retryer;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@NoArgsConstructor
public class CustomRetryer implements Retryer {

    private int retryMaxAttempt;
    private long retryInterval;
    private int attempt = 1;

    public CustomRetryer(int retryMaxAttempt, long retryInterval) {
        this.retryMaxAttempt = retryMaxAttempt;
        this. retryInterval = retryInterval;
    }


    @Override
    public void continueOrPropagate(RetryableException exception) {
        log.info("Feign retry attempt {} due to {} ", attempt, exception.getMessage());

        if (attempt++ == retryMaxAttempt) {
            throw exception;
        }
        try {
            Thread.sleep(retryInterval);
        } catch (InterruptedException ignored) {
            Thread.currentThread().interrupt();;
        }
    }

    @Override
    public Retryer clone() {
        return new CustomRetryer(6, 1000L);
    }
}
