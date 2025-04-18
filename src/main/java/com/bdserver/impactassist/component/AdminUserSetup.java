package com.bdserver.impactassist.component;

import com.bdserver.impactassist.model.RegisterUserDAO;
import com.bdserver.impactassist.repo.UserRepo;
import com.bdserver.impactassist.repo.UserRoleRepo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdminUserSetup implements CommandLineRunner {
    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(16);
    private final UserRepo userRepo;
    private final UserRoleRepo userRoleRepo;
    AuthenticationManager authenticationManager;

    public AdminUserSetup(UserRepo userRepo, UserRoleRepo userRoleRepo, AuthenticationManager authenticationManager) {
        this.userRepo = userRepo;
        this.userRoleRepo = userRoleRepo;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public void run(String... args) throws Exception {
        if (userRepo.findByUsername("admin@email.com") == null) {
            RegisterUserDAO user = new RegisterUserDAO("admin", "admin", "admin@email.com", "+5456515651651651");
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            Integer userId = userRepo.createUser(user);
            System.out.println(userId);
            userRoleRepo.createUser(userId, 4);
        }
    }
}
