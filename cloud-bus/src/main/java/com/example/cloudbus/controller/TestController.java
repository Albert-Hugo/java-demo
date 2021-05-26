package com.example.cloudbus.controller;

import com.example.cloudbus.service.TestService;
import com.oppein.framework.dto.DataResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Ido
 * @date 2021/4/26 14:32
 */
@RequestMapping
@RestController
public class TestController {
    @Autowired
    private TestService testService;
    @GetMapping("/get")
    public DataResponse get() {
        testService.test();
        return DataResponse.of(null);
    }

}
