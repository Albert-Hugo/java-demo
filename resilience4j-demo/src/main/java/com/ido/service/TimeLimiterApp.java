package com.ido.service;

import com.sun.xml.internal.ws.util.CompletedFuture;
import io.github.resilience4j.timelimiter.TimeLimiter;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

/**
 * @author Ido
 * @date 2021/5/27 11:15
 */
public class TimeLimiterApp {
    public static void main(String[] args) throws Exception {
        TimeLimiterConfig config = TimeLimiterConfig.custom()
                .cancelRunningFuture(true)
                .timeoutDuration(Duration.ofMillis(500))
                .build();

        TimeLimiter timeLimiter = TimeLimiter.of(config);

        InheritableThreadLocal<String> threadLocal = new InheritableThreadLocal<>();
        threadLocal.set("ddddd");
        String result = timeLimiter.executeFutureSupplier(() -> CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(200);
                System.out.println(threadLocal.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "fsgs";
        }));

        System.out.println(result);
    }
}
