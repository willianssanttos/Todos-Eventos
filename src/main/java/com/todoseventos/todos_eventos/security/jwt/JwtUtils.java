package com.todoseventos.todos_eventos.security.jwt;

import com.todoseventos.todos_eventos.exception.CustomException;
import com.todoseventos.todos_eventos.usecase.UserDetailsImpl;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtils {

    @Value("${spring.application.projeto.jwtSecret}")
    private String jwtSecret;

    @Value("${spring.application.projeto.jwtExpirationMs}")
    private int jwtExpirationMs;

    public String generateJwtToken(Authentication authentication) {
        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
        return Jwts.builder()
                .setSubject(userPrincipal.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(getSigninKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser().setSigningKey(getSigninKey()).build().parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(getSigninKey()).build().parseClaimsJws(authToken);
            return true;
        } catch (MalformedJwtException e) {
            throw new CustomException("Token inválido: " + e.getMessage());
        } catch (ExpiredJwtException e) {
            throw new CustomException("Token expirado: " + e.getMessage());
        } catch (UnsupportedJwtException e) {
            throw new CustomException("Token não suportado: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            throw new CustomException("Token Argumento inválido: " + e.getMessage());
        }
    }

    private Key getSigninKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }
}
