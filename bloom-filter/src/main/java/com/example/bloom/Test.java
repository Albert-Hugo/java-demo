package com.example.bloom;

import com.sun.xml.internal.ws.util.CompletedFuture;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * @author Ido
 * @date 2020/8/21 14:47
 */
public class Test {


    public static void main(String[] args) throws InterruptedException {
        CompletableFuture<String> cf1 = CompletableFuture.supplyAsync(Test::queryCode);
        cf1.thenAcceptAsync((code)->queryData(code));
//        CompletableFuture cf2 =  CompletableFuture.supplyAsync(Test::queryCode2);



//        CompletableFuture all = CompletableFuture.allOf(cf1,cf2);
//        try {
//            System.out.println(all.get());;
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        }
//        try {
//            System.out.println(cf1.get());
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        }
        System.out.println("run");
        Thread.sleep(10000);
    }

    static String queryCode() {
        try {
            Thread.sleep(1000);
            throw new RuntimeException();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "1";

    }

    static String queryCode2() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "2";

    }

    static String queryData(String code) {
        System.out.println(code);

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "code:" + code;

    }
}
