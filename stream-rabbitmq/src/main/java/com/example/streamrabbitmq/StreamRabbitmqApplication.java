package com.example.streamrabbitmq;

import com.oppein.framework.dto.DataResponse;
import com.oppein.framework.dto.Response;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
@RequestMapping("")
public class StreamRabbitmqApplication {
    static int count = 0;
    public static void main(String[] args) {
        SpringApplication.run(StreamRabbitmqApplication.class, args);
    }


    @GetMapping("/get/23")
    public Response test() {
//        System.out.println(count++);
        return DataResponse.of("2134");
//        throw new RuntimeException();
    }


}
