package com.bdserver.impactassist.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterUserDAO {
    @NotNull
    @Size(min = 1, max = 100)
    private String fullName;
    @NotNull
    @Size(min = 8, max = 100)
    private String password;
    @NotNull
    @Email
    @Size(min = 1, max = 100)
    private String email;
}

