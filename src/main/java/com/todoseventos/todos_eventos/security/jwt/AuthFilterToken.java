package com.todoseventos.todos_eventos.security.jwt;

import com.todoseventos.todos_eventos.exception.CustomException;
import com.todoseventos.todos_eventos.usecase.DetalheUsuarioServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class AuthFilterToken extends OncePerRequestFilter {

    @Autowired
    private JwtUtils jwtUtil;

    @Autowired
    private DetalheUsuarioServiceImpl userDetailService;

    /**
     * Filtra cada requisição para verificar a presença de um token JWT válido.
     * @param request O objeto HttpServletRequest.
     * @param response O objeto HttpServletResponse.
     * @param filterChain O objeto FilterChain.
     * @throws ServletException se ocorrer um erro de servlet.
     * @throws IOException se ocorrer um erro de I/O.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String jwt = getToken(request);
            if(jwt != null && jwtUtil.validateJwtToken(jwt)) {

                String username = jwtUtil.getUsernameToken(jwt);

                UserDetails userDetails = userDetailService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userDetails,  null, userDetails.getAuthorities());
                auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(auth);
            }

        }catch(Exception e) {
            throw new CustomException("Ocorreu um erro ao proecssar o token.");
        }

        filterChain.doFilter(request, response);
    }

    /**
     * Obtém o token JWT do cabeçalho da requisição.
     * @param request O objeto HttpServletRequest.
     * @return O token JWT, ou null se não estiver presente.
     */
    private String getToken(HttpServletRequest request) {
        String headerToken = request.getHeader("Autorização");
        if(StringUtils.hasText(headerToken) && headerToken.startsWith("Bearer")) {
            return headerToken.replace("Bearer ","");
        }
        return null;
    }
}
