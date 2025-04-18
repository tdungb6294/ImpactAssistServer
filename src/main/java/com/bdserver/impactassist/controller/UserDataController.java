package com.bdserver.impactassist.controller;

import com.bdserver.impactassist.model.ResponseUserDataDAO;
import com.bdserver.impactassist.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("user")
public class UserDataController {
    private final UserService userService;

    public UserDataController(UserService userService) {
        this.userService = userService;
    }

    @PreAuthorize("hasAuthority('LOCAL_EXPERT') or hasAuthority('ADMIN')")
    @GetMapping("/{id}")
    public ResponseUserDataDAO getUserData(@PathVariable int id) {
        return userService.getUserDataById(id);
    }
}
