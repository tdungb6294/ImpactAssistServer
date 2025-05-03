package com.bdserver.impactassist.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class RegisterUserDAO {
    @NotBlank
    @Size(min = 1, max = 100)
    private String fullName;
    @NotBlank
    @Size(min = 8, max = 100)
    private String password;
    @NotBlank
    @Email
    @Size(min = 1, max = 100)
    private String email;
    @Size(min = 1, max = 100)
    private String phone;
}

