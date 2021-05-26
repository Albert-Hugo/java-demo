package com.example.cloudbus;

import com.example.cloudbus.service.TestService;
import com.oppein.bsp.behaviorcenter.dto.cmd.evaluation.EvaluateCmd;
import com.oppein.framework.dto.Command;
import com.oppein.framework.dto.DataResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.xml.crypto.Data;
import java.util.Map;

@SpringBootApplication
@RestController
@RequestMapping("")
public class CloudBusApplication {


    @GetMapping("test")
    public String test() throws InterruptedException {
        System.out.println("test");
        return "test";

    }

    public static void main(String[] args) {
        SpringApplication.run(CloudBusApplication.class, args);
    }




}
