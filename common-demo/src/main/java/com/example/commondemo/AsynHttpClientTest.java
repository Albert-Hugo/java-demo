package com.example.commondemo;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Ido
 * @date 2021/1/29 13:19
 */
public class AsynHttpClientTest {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        CloseableHttpAsyncClient httpclient = HttpAsyncClients.createDefault();
        ExecutorService executorService = Executors.newFixedThreadPool(100);
        try {
            // Start the client
            httpclient.start();
            int count = 10000;
            // One most likely would want to use a callback for operation result
            final CountDownLatch latch = new CountDownLatch(count);
            long start = System.currentTimeMillis();


            final HttpGet request = new HttpGet("http://localhost:8888/get/1");
            for (int i = 0; i < count; i++) {
                CompletableFuture.runAsync(()->{

//                asynGet(httpclient,latch);
                commonGet(latch);
                },executorService);
            }

            latch.await();
            long end = System.currentTimeMillis();

            System.out.println(end - start);

        } finally {
            try {
                httpclient.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            executorService.shutdown();
        }
    }
    public static void commonGet(final CountDownLatch latch ){
        String result = HttpUtils.httpGet("http://localhost:8888/get/1", new HashMap<>(),"utf-8");
        latch.countDown();
    }

    public static void asynGet(CloseableHttpAsyncClient httpclient,final CountDownLatch latch ) {
        final HttpGet request = new HttpGet("http://localhost:8888/get/1");
        httpclient.execute(request, new FutureCallback<HttpResponse>() {

            @Override
            public void completed(final HttpResponse response2) {
                latch.countDown();
//                System.out.println(request.getRequestLine() + "->" + response2.getStatusLine());
            }

            @Override
            public void failed(final Exception ex) {
                latch.countDown();
                System.out.println(request.getRequestLine() + "->" + ex);
            }

            @Override
            public void cancelled() {
                latch.countDown();
                System.out.println(request.getRequestLine() + " cancelled");
            }

        });
    }
}
