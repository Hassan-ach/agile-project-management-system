package com.ensa.agile.infrastructure.security.service;

import com.ensa.agile.domain.user.entity.User;
import com.ensa.agile.infrastructure.security.exception.JwtCreationException;
import com.ensa.agile.infrastructure.security.exception.JwtValidationException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import javax.crypto.SecretKey;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class JwtService {
    @Value("${security.jwt.access.expiration-hours}")
    private Long JWT_EXPIRATION_DATE;

    @Value("${security.jwt.refresh.expiration-hours}")
    private Long JWT_REFRESH_EXPIRATION_DATE;

    @Value("${security.jwt.secret.key}") private String JWT_SECRET_KEY;

    private String buildToken(Map<String, Object> extraClaims, String subject,
                              Long expirationHours) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationHours * 3600_000L);
        try {
            return Jwts.builder()
                .claims(extraClaims)
                .subject(subject)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(getSigningKey())
                .compact();
        } catch (Exception e) {
            throw new JwtCreationException();
        }
    }

    public String generateToken(User user) {
        return buildToken(new HashMap<>(), user.getEmail(),
                          JWT_EXPIRATION_DATE);
    }

    public String generateToken(String email) {
        return buildToken(new HashMap<>(), email, JWT_EXPIRATION_DATE);
    }
    public String generateRefreshToken(String token) {
        String email = extractEmail(token);
        return buildToken(new HashMap<>(), email, JWT_REFRESH_EXPIRATION_DATE);
    }

    public String generateToken(Map<String, Object> extraClaims, User user) {
        return buildToken(extraClaims, user.getEmail(), JWT_EXPIRATION_DATE);
    }

    public String generateToken(Map<String, Object> extraClaims, String email) {
        return buildToken(extraClaims, email, JWT_EXPIRATION_DATE);
    }

    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private Claims extractAllClaims(String token) {
        try {
            return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        } catch (Exception e) {
            throw new JwtValidationException();
        }
    }

    private <T> T extractClaim(String token,
                               Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public boolean isTokenValid(String token, User user) {
        return extractEmail(token).equals(user.getEmail()) &&
            !isTokenExpired(token);
    }

    public boolean isTokenExpired(String token) {
        return extractExpirationDate(token).before(new Date());
    }

    private Date extractExpirationDate(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = JWT_SECRET_KEY.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
