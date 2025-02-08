package com.bdserver.impactassist.controller;

import com.bdserver.impactassist.model.UserPrincipal;
import com.bdserver.impactassist.repo.UserRepo;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("hello")
public class TestController {
    private final UserRepo userRepo;

    public TestController(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public String hello() {
        return "Hello World";
    }

    @GetMapping("/world")
    public String hello2() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            return String.valueOf(userRepo.findByUsername(userPrincipal.getUsername()).getId());
        } else {
            throw new UsernameNotFoundException("Not authenticated");
        }
    }
}
