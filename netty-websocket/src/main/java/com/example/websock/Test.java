package com.example.websock;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;

/**
 * @author Ido
 * @date 2020/8/27 17:37
 */
public class Test {
    public static void main(String[] args) throws IOException, InterruptedException {
        for(int i  = 0; i < 10; i++){

            new Thread(()->{
                HttpClient client = HttpClientBuilder.create().build();
                try {
                    HttpResponse clientRsp = client.execute(new HttpGet("http://localhost:8888"));
                    byte[] bs = new byte[clientRsp.getEntity().getContent().available()];
                    clientRsp.getEntity().getContent().read(bs);
                    System.out.println(new String(bs,"utf-8"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
        }


        Thread.sleep(100000);
    }
}
