package com.bdserver.impactassist.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JwtTokenDAO {
    private String accessToken;
    private String refreshToken;
    private String role;
}
