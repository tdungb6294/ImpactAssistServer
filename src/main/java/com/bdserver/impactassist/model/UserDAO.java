package com.bdserver.impactassist.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserDAO {
    private int id;
    private String fullName;
    private String password;
    private String email;
    private String phone;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
