package com.bdserver.impactassist.service;

import com.bdserver.impactassist.TestConfig;
import com.bdserver.impactassist.TestcontainersConfiguration;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@RunWith(SpringRunner.class)
@Import({TestcontainersConfiguration.class, TestConfig.class})
class JwtServiceTest {

    @Autowired
    private JwtService jwtService;

    @Test
    void validateAccessToken() {
        Map<String, Object> claims = new HashMap<>();
        String accessToken = Jwts.builder()
                .claims()
                .add(claims)
                .subject(String.valueOf(1))
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis()))
                .and()
                .signWith(jwtService.getAccessKeyPair().getPrivate(), Jwts.SIG.EdDSA)
                .compact();
        assertThrows(ExpiredJwtException.class, () -> jwtService.validateAccessToken(accessToken));
    }

    @Test
    void validateRefreshToken() {
        Map<String, Object> claims = new HashMap<>();
        String refreshToken = Jwts.builder()
                .claims()
                .add(claims)
                .subject(String.valueOf(1))
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis()))
                .and()
                .signWith(jwtService.getRefreshKeyPair().getPrivate(), Jwts.SIG.EdDSA)
                .compact();
        assertThrows(ExpiredJwtException.class, () -> jwtService.validateRefreshToken(refreshToken));
    }
}