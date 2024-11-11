package com.Todo.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.List;

@Slf4j
@Component
public class JwtUtil { //todo 작동 원리 찾기

    public static String getUsername(String token, String key){
        return extractClaims(token,key).get("username",String.class);
    }

    public static boolean isExpired(String token, String key){
        Date expireDate = extractClaims(token, key).getExpiration();
        return expireDate.before(new Date());
    }

    private static Claims extractClaims(String token, String key){
        return Jwts.parserBuilder().setSigningKey(getKey(key)).build().parseClaimsJws(token).getBody();
    }

    public static String generateToken(String username, String key, long expiredTimeMs){
        Claims claims = Jwts.claims();
        claims.put("username",username);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiredTimeMs))
                .signWith(getKey(key), SignatureAlgorithm.HS256)
                .compact();
    }


    private static Key getKey(String key) {
        byte[] bytes = key.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(bytes);
    }

}
