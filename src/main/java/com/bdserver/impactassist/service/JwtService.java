package com.bdserver.impactassist.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.impl.lang.Function;
import org.springframework.stereotype.Service;

import java.security.KeyPair;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtService {

    private final KeyPair accessKeyPair;
    private final KeyPair refreshKeyPair;

    public JwtService() {
        accessKeyPair = Jwts.SIG.EdDSA.keyPair().build();
        refreshKeyPair = Jwts.SIG.EdDSA.keyPair().build();
        System.out.println(accessKeyPair.getPublic().toString());
    }

    public String generateAccessToken(int userId) {
        Map<String, Object> claims = new HashMap<>();
        return Jwts.builder()
                .claims()
                .add(claims)
                .subject(String.valueOf(userId))
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 10))
                .and()
                .signWith(accessKeyPair.getPrivate(), Jwts.SIG.EdDSA)
                .compact();
    }

    public Integer extractUserId(String token) {
        return Integer.parseInt(extractClaim(token, Claims::getSubject));
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public Claims extractAllClaims(String token) {
        return Jwts
                .parser()
                .verifyWith(accessKeyPair.getPublic())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean validateAccessToken(String token) {
        return !isTokenExpired(token);
    }

    public boolean validateRefreshToken(String token) {
        return !isRefreshTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        Date expirationDate = extractClaim(token, Claims::getExpiration);
        return expirationDate.before(new Date());
    }

    private boolean isRefreshTokenExpired(String token) {
        Date expirationDate = extractRefreshClaim(token, Claims::getExpiration);
        return expirationDate.before(new Date());
    }

    public String generateRefreshToken(int userId) {
        Map<String, Object> claims = new HashMap<>();
        return Jwts.builder()
                .claims()
                .add(claims)
                .subject(String.valueOf(userId))
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 7))
                .and()
                .signWith(refreshKeyPair.getPrivate(), Jwts.SIG.EdDSA)
                .compact();
    }

    public Integer extractRefreshUserId(String token) {
        return Integer.parseInt(extractRefreshClaim(token, Claims::getSubject));
    }

    public <T> T extractRefreshClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractRefreshAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public Claims extractRefreshAllClaims(String token) {
        return Jwts
                .parser()
                .verifyWith(refreshKeyPair.getPublic())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
