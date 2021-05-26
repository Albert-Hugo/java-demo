package com.example.commondemo;

import com.example.commondemo.service.TestService;

import java.util.ServiceLoader;

/**
 * @author Ido
 * @date 2020/9/23 14:55
 */
public class App {
    public static void main(String[] args) {

        ServiceLoader<TestService> testServiceList = ServiceLoader.load(TestService.class);
        for (TestService s : testServiceList) {
            s.print();
        }


    }

}
