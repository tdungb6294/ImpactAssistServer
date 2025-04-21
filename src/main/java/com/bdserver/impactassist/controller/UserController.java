package com.bdserver.impactassist.controller;

import com.bdserver.impactassist.model.*;
import com.bdserver.impactassist.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("auth")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public Integer register(@Valid @RequestBody RegisterUserDAO userDAO) {
        return userService.registerUser(userDAO);
    }

    @PostMapping("/login")
    public JwtTokenDAO login(@Valid @RequestBody LoginUserDAO loginUserDAO) {
        return userService.verify(loginUserDAO);
    }

    @GetMapping("/users")
    public List<UserDAO> getUsers() {
        return userService.userList();
    }

    @PostMapping("/refresh")
    public String refresh(HttpServletRequest request) {
        return userService.getNewAccessToken(request);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/local-expert")
    public Integer registerLocalExpert(@Valid @RequestBody RegisterLocalExpertDAO user) {
        return userService.registerLocalExpertUser(user);
    }
}
