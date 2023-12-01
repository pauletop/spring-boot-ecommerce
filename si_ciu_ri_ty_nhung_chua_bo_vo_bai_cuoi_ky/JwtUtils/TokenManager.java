package org.example.Lab9_10.JwtUtils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.Serial;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
public class TokenManager implements Serializable {
    private static final long VALIDITY = 10 * 60 * 60;

    @Value("${secret}")
    private String SECRET;

    public Key getSingingKey() {
        String encodedKey = Base64.getEncoder().encodeToString(SECRET.getBytes(StandardCharsets.UTF_8));
        byte[] byteKey = Decoders.BASE64.decode(encodedKey);
        return Keys.hmacShaKeyFor(byteKey);
    }

    public String createJwt(UserDetails ud) {
        return Jwts.builder()
                .setSubject(ud.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + VALIDITY * 1000))
                .signWith(getSingingKey())
                .compact();
    }

    public boolean validateJwt(String token, UserDetails ud) {
        String us = getUsernameFromToken(token);
        Claims claims = Jwts.parser()
                .setSigningKey(getSingingKey())
                .parseClaimsJws(token)
                .getBody();
        boolean isExpired = claims.getExpiration().before(new Date());
        return (us.equals(ud.getUsername()) && !isExpired);
    }

    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(getSingingKey())
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }
}