package com.bdserver.impactassist.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.impl.lang.Function;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtService {

    private final String accessSecretKey;
    private final String refreshSecretKey;

    public JwtService() {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance("HmacSHA256");
            SecretKey key = keyGenerator.generateKey();
            accessSecretKey = Base64.getEncoder().encodeToString(key.getEncoded());
            key = keyGenerator.generateKey();
            refreshSecretKey = Base64.getEncoder().encodeToString(key.getEncoded());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
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
                .signWith(getAccessKey(), Jwts.SIG.HS256)
                .compact();
    }

    private SecretKey getAccessKey() {
        byte[] keyBytes = Decoders.BASE64.decode(accessSecretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private SecretKey getRefreshKey() {
        byte[] keyBytes = Decoders.BASE64.decode(refreshSecretKey);
        return Keys.hmacShaKeyFor(keyBytes);
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
                .verifyWith(getAccessKey())
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
                .signWith(getRefreshKey(), Jwts.SIG.HS256)
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
                .verifyWith(getRefreshKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
