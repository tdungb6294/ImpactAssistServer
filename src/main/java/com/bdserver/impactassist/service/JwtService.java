package com.bdserver.impactassist.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.impl.lang.Function;
import lombok.Getter;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@Getter
public class JwtService {
    private final Environment environment;
    private final KeyPair accessKeyPair;
    private final KeyPair refreshKeyPair;

    public JwtService(Environment environment) throws NoSuchAlgorithmException, InvalidKeySpecException {
        this.environment = environment;
        KeyFactory keyFactory = KeyFactory.getInstance("Ed25519");
        byte[] decoded = Base64.getDecoder().decode(environment.getProperty("jwt.access.public-key"));
        byte[] decoded2 = Base64.getDecoder().decode(environment.getProperty("jwt.access.private-key"));
        byte[] decoded3 = Base64.getDecoder().decode(environment.getProperty("jwt.refresh.public-key"));
        byte[] decoded4 = Base64.getDecoder().decode(environment.getProperty("jwt.refresh.private-key"));
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decoded);
        PKCS8EncodedKeySpec keySpec2 = new PKCS8EncodedKeySpec(decoded2);
        X509EncodedKeySpec keySpec3 = new X509EncodedKeySpec(decoded3);
        PKCS8EncodedKeySpec keySpec4 = new PKCS8EncodedKeySpec(decoded4);
        accessKeyPair = new KeyPair(keyFactory.generatePublic(keySpec), keyFactory.generatePrivate(keySpec2));
        refreshKeyPair = new KeyPair(keyFactory.generatePublic(keySpec3), keyFactory.generatePrivate(keySpec4));
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
