package com.example.commondemo;


import com.ido.robin.client.RobinClient;

/**
 * @author Ido
 * @date 2020/9/24 11:24
 */
public class RobinClientTest {

    public static void main(String[] args) {
        RobinClient robinClient = new RobinClient("localhost", 8688);
        System.out.println(robinClient.get("ido"));
        ;
        robinClient.delete("ido");
        System.out.println(robinClient.get("ido"));
        ;
        robinClient.shutdown();
    }
}
