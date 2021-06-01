package com.example.choppercache;

import com.ido.op.chopper.EnableChopperCache;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableChopperCache
public class ChopperCacheApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChopperCacheApplication.class, args);
    }

}
