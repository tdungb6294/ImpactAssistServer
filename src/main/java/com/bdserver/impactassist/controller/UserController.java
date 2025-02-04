package com.bdserver.impactassist.controller;

import com.bdserver.impactassist.model.RegisterUserDAO;
import com.bdserver.impactassist.model.UserDAO;
import com.bdserver.impactassist.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public Integer register(@RequestBody RegisterUserDAO userDAO) {
        return userService.registerUser(userDAO);
    }

    @GetMapping("/users")
    public List<UserDAO> getUsers() {
        return userService.userList();
    }
}
