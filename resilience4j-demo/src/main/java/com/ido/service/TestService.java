package com.ido.service;

/**
 * @author Ido
 * @date 2021/5/26 14:56
 */
public class TestService {

    public String print(int i) {
        if (i % 2 == 0) {

            System.out.println("hello");
            return "";
        }
        return "1";
//        throw new RuntimeException();
    }
}
