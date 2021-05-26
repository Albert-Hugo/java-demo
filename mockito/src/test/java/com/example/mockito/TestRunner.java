package com.example.mockito;

import io.netty.util.internal.ConcurrentSet;
import org.mockito.Mockito;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Ido
 * @date 2020/8/19 15:42
 */
public class TestRunner {


    public static void main(String[] args) {
        HashSet set  = new HashSet();
        System.out.println(set.add(1));
        System.out.println(set.add(1));
    }
}
