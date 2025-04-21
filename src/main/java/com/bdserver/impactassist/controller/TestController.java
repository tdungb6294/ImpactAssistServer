package com.bdserver.impactassist.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("hello")
public class TestController {

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public String hello() {
        return "Hello World";
    }
}
