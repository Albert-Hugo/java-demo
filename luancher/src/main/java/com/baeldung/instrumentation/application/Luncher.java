package com.baeldung.instrumentation.application;

/**
 * @author Ido
 * @date 2020/9/3 16:38
 */
public class Luncher {

    public static void main(String[] args) {
        new MyAtm().withdraw();
    }
}
