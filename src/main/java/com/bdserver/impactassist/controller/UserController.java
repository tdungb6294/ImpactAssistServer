package com.bdserver.impactassist.controller;

import com.bdserver.impactassist.model.JwtTokenDAO;
import com.bdserver.impactassist.model.LoginUserDAO;
import com.bdserver.impactassist.model.RegisterUserDAO;
import com.bdserver.impactassist.model.UserDAO;
import com.bdserver.impactassist.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
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
    public Integer register(@RequestBody RegisterUserDAO userDAO) {
        return userService.registerUser(userDAO);
    }

    @PostMapping("/login")
    public JwtTokenDAO login(@RequestBody LoginUserDAO loginUserDAO) {
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
}
