package com.todoseventos.todos_eventos.usecase;

import com.todoseventos.todos_eventos.dto.AcessDTO;
import com.todoseventos.todos_eventos.dto.AuthenticationDTO;
import com.todoseventos.todos_eventos.exception.CustomException;
import com.todoseventos.todos_eventos.security.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    /**
     * Realiza o login do usuário.
     * @param authDto Objeto contendo as credenciais de autenticação (e-mail e senha).
     * @return Um objeto AcessDTO contendo o token JWT gerado.
     * @throws CustomException se as credenciais forem inválidas ou ocorrer um erro interno.
     */
    public AcessDTO login(AuthenticationDTO authDto) {
        try {
            // Cria um token de autenticação usando o e-mail e a senha fornecidos
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(authDto.getEmail(), authDto.getSenha());

            // Autentica o token
            Authentication authentication = authenticationManager.authenticate(authenticationToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Gera um token JWT com base na autenticação
            String jwt = jwtUtils.generateJwtToken(authentication);

            // Retorna o token JWT
            return new AcessDTO(jwt);
        } catch (BadCredentialsException e) {
            // Lança uma exceção personalizada se as credenciais forem inválidas
            throw new CustomException(CustomException.EMIAL_SENHA);
        } catch (Exception e) {
            // Lança uma exceção personalizada se ocorrer um erro interno
            throw new CustomException(CustomException.ERRO_INTERNO);
        }
    }
}
