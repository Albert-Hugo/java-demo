package com.ido.service;

import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import io.github.resilience4j.retry.RetryRegistry;

import java.time.Duration;

/**
 * @author Ido
 * @date 2021/5/27 13:56
 */
public abstract class RetrySupport {

    RetryConfig config = RetryConfig.custom()
            .maxAttempts(5)
            .waitDuration(Duration.ofMillis(500))
            .retryExceptions(RuntimeException.class)
            .build();
    RetryRegistry retryRegistry = RetryRegistry.of(config);
    Retry retry = retryRegistry.retry("default");

    public void execute() {
        Runnable supplier = Retry.decorateRunnable(retry, this::run);
        supplier.run();

    }


    abstract protected void run();


}
