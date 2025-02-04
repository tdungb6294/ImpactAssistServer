package com.bdserver.impactassist.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/hello")
public class TestController {
    @GetMapping()
    public String hello() {
        return "Hello World";
    }
}
