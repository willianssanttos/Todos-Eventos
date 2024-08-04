package com.todoseventos.todos_eventos.security.jwt;

import com.todoseventos.todos_eventos.usecase.UserDetailsImpl;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtils {

    @Value("${spring.application.projeto.jwtSecret}")
    private String jwtSecret;

    @Value("${spring.application.projeto.jwtExpirationMs}")
    private int jwtExpirationMs;

    /**
     * Gera um token JWT a partir dos detalhes do usuário.
     * @param userDetail Os detalhes do usuário.
     * @return O token JWT gerado.
     */
    public String generateTokenFromUserDetailsImpl(UserDetailsImpl userDetail){
        return Jwts.builder().setSubject(userDetail.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + jwtExpirationMs))
                .signWith(getSigninKey(), SignatureAlgorithm.HS512).compact();

    }

    /**
     * Obtém a chave de assinatura para o JWT a partir do segredo configurado.
     * @return A chave de assinatura.
     */
    public Key getSigninKey(){
        SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
        return key;
    }

    /**
     * Obtém o nome de usuário (subject) a partir do token JWT.
     * @param token O token JWT.
     * @return O nome de usuário contido no token.
     */
    public String getUsernameToken(String token) {
        return Jwts.parser().setSigningKey(getSigninKey()).build()
                .parseClaimsJws(token).getBody().getSubject();
    }

    /**
     * Obtém o nome de usuário (subject) a partir do token JWT.
     * @param token O token JWT.
     * @return O nome de usuário contido no token.
     */
    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(getSigninKey()).build().parseClaimsJws(authToken);
            return true;
        } catch (MalformedJwtException e) {
            System.out.println("Token inválido " + e.getMessage());
        } catch (ExpiredJwtException e) {
            System.out.println("Token expirado " + e.getMessage());
        }catch (UnsupportedJwtException e) {
            System.out.println("Token não suportado " + e.getMessage());
        }catch (IllegalArgumentException e) {
            System.out.println("Token Argumento invalido " + e.getMessage());
        }
        return false;
    }
}
