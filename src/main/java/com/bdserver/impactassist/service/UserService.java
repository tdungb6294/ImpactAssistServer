package com.bdserver.impactassist.service;

import com.bdserver.impactassist.model.RegisterUserDAO;
import com.bdserver.impactassist.model.UserDAO;
import com.bdserver.impactassist.repo.UserRepo;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(16);
    private final UserRepo userRepo;

    public UserService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    public Integer registerUser(RegisterUserDAO user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        System.out.println(user);
        return userRepo.createUser(user);
    }

    public List<UserDAO> userList() {
        return userRepo.findAll();
    }
}
