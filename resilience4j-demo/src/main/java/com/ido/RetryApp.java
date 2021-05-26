package com.ido;

import com.ido.service.TestService;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.core.SupplierUtils;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import io.github.resilience4j.retry.RetryRegistry;

import java.time.Duration;
import java.util.concurrent.Callable;
import java.util.function.Supplier;

/**
 * @author Ido
 * @date 2021/5/26 14:50
 */
public class RetryApp {
    public static void main(String[] args) {
        Supplier<String> supplier = () -> {
//            for (int i = 0; i < 10; i++) {
//                testService.print(i);
//            }
            System.out.println("1");
            throw new RuntimeException();
        };


//        supplier = SupplierUtils.andThen(supplier, (result, exception) -> {
//            System.out.println(result);
//            return "recovery from exception";
//        });
        RetryConfig config = RetryConfig.custom()
                .maxAttempts(5)
                .waitDuration(Duration.ofMillis(500))
                .retryExceptions(RuntimeException.class)
                .build();
        RetryRegistry retryRegistry  =  RetryRegistry.of(config);
        Retry retry = retryRegistry.retry("default");
        supplier = Retry.decorateSupplier(retry,supplier);
        supplier.get();
        ;
    }
}
