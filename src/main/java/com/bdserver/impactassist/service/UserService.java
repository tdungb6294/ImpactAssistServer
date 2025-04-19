package com.bdserver.impactassist.service;

import com.bdserver.impactassist.model.*;
import com.bdserver.impactassist.repo.UserRepo;
import com.bdserver.impactassist.repo.UserRoleRepo;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {
    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(16);
    private final UserRepo userRepo;
    private final JwtService jwtService;
    private final UserRoleRepo userRoleRepo;
    AuthenticationManager authenticationManager;

    public UserService(AuthenticationManager authenticationManager, UserRoleRepo userRoleRepo, JwtService jwtService, UserRepo userRepo) {
        this.authenticationManager = authenticationManager;
        this.userRoleRepo = userRoleRepo;
        this.jwtService = jwtService;
        this.userRepo = userRepo;
    }

    @Transactional
    public Integer registerUser(RegisterUserDAO user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        Integer userId = userRepo.createUser(user);
        userRoleRepo.createUser(userId, 1);
        return userId;
    }

    public List<UserDAO> userList() {
        return userRepo.findAll();
    }

    public JwtTokenDAO verify(LoginUserDAO loginUserDAO) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginUserDAO.getEmail(), loginUserDAO.getPassword()));
        UserDAO user = userRepo.findByUsername(loginUserDAO.getEmail());
        if (authentication.isAuthenticated() && user != null) {
            String accessToken = jwtService.generateAccessToken(user.getId());
            String refreshToken = jwtService.generateRefreshToken(user.getId());
            String role = userRoleRepo.getUserRoleNameByUserId(user.getId());
            return new JwtTokenDAO(accessToken, refreshToken, role);
        }

        throw new UsernameNotFoundException("User not found");
    }

    public String getNewAccessToken(HttpServletRequest request) {
        if (request.getCookies() != null) {
            for (var cookie : request.getCookies()) {
                if ("refreshToken".equals(cookie.getName())) {
                    String refreshToken = cookie.getValue();
                    if (refreshToken != null && jwtService.validateRefreshToken(refreshToken)) {
                        Integer userId = jwtService.extractRefreshUserId(refreshToken);
                        return jwtService.generateAccessToken(userId);
                    }
                }
            }
        }
        throw new UsernameNotFoundException("Refresh token not found");
    }

    public int getUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            return userRepo.findByUsername(userPrincipal.getUsername()).getId();
        } else {
            throw new UsernameNotFoundException("Not authenticated");
        }
    }

    @Transactional
    public Integer registerLocalExpertUser(RegisterLocalExpertDAO user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        Integer userId = userRepo.createUser(new RegisterUserDAO(user.getFullName(), user.getPassword(), user.getEmail(), user.getPhone()));
        userRoleRepo.createUser(userId, 2);
        user.setUserId(userId);
        userRepo.createLocalExpert(user);
        return userId;
    }

    public String getHashedPassword() {
        return bCryptPasswordEncoder.encode("sillybilly");
    }

    public ResponseUserDataDAO getUserDataById(int id) {
        return userRepo.findUserDataById(id);
    }
}
