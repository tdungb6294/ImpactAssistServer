package com.bdserver.impactassist.controller;

import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("hello")
public class TestController {

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    @RateLimiter(name = "myRateLimiter", fallbackMethod = "fallbackMethod")
    public String hello() {
        return "Hello World";
    }

    public String fallbackMethod(Exception ex) {
        return "Rate limit exceeded, please try again later!";
    }
}
