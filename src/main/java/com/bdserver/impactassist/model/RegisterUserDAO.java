package com.bdserver.impactassist.model;

import lombok.Data;

@Data
public class RegisterUserDAO {
    private String fullName;
    private String password;
    private String email;
}

