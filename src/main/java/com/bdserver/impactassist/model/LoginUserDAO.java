package com.bdserver.impactassist.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LoginUserDAO {
    @NotNull
    @Size(min = 1, max = 100)
    private String email;
    @NotNull
    @Size(min = 1, max = 100)
    private String password;
}
