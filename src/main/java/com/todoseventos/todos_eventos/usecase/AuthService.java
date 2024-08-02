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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public AcessDTO login(AuthenticationDTO authDto) {
        try {
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(authDto.getEmail(), authDto.getSenha());

            Authentication authentication = authenticationManager.authenticate(authenticationToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String jwt = jwtUtils.generateTokenFromUserDetailsImpl((UserDetailsImpl) userDetails);

            return new AcessDTO(jwt);
        } catch (BadCredentialsException e) {
            throw new CustomException("Email ou senha inválidos.");
        } catch (Exception e) {
            throw new CustomException("Ocorreu um erro interno. Por favor, tente novamente mais tarde.");
        }
    }
}
