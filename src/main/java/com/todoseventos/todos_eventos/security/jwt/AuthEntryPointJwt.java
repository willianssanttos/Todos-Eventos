package com.todoseventos.todos_eventos.security.jwt;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class AuthEntryPointJwt implements AuthenticationEntryPoint {
    /**
     * Inicia a resposta HTTP quando uma solicitação não autenticada tenta acessar um recurso protegido.
     * @param request O objeto HttpServletRequest.
     * @param response O objeto HttpServletResponse.
     * @param authException A exceção de autenticação que ocorreu.
     * @throws IOException se ocorrer um erro de I/O.
     * @throws ServletException se ocorrer um erro de servlet.
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {

        // Define o tipo de conteúdo da resposta como JSON
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        // Define o status da resposta como 401 Não Autorizado
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        // Cria um mapa para armazenar os detalhes do erro
        final Map<String, Object> body = new HashMap<>();
        body.put("status", HttpServletResponse.SC_UNAUTHORIZED);
        body.put("error", "Unauthorized");

        // Converte o mapa em JSON e escreve na resposta
        final ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getOutputStream(), body);
    }
}
