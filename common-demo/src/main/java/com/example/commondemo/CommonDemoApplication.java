package com.example.commondemo;

import com.oppein.bsp.behaviorcenter.api.evaluation.EvaluateResource;
import com.oppein.bsp.ordercenter.api.OrderResource;
import com.oppein.miop.search.dto.clientobject.IndexCatCO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.scope.refresh.RefreshScope;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@SpringBootApplication
@org.springframework.cloud.context.config.annotation.RefreshScope
@Configuration
@EnableFeignClients
public class CommonDemoApplication {

    @FeignClient("test")
    public interface  TestResource extends OrderResource {

    }

    @Autowired
    TestResource testResource;

    @Service("test")
    public static class Test{

        public void say(){
            System.out.println("hello");
        }

        @PostConstruct
        public void init(){
            System.out.println("init");
        }
    }

    @Autowired
    private Test test;

    @GetMapping("home")
    public String home(HttpServletResponse response) throws IOException, InterruptedException {
        Thread.sleep(1000 * 5);

        IndexCatCO catCO= new IndexCatCO();
        return "home";
    }

    @PostMapping("webhook/alert")
    public String webhook(HttpServletResponse response) throws IOException, InterruptedException {
        System.out.println("alert");
        return "home";
    }


    public static class Person {
        public int age;
        public String name;

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }


    public static void main(String[] args) {
        SpringApplication.run(CommonDemoApplication.class, args);
    }

}
