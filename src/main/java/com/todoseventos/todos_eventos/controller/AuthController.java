package com.todoseventos.todos_eventos.controller;

import com.todoseventos.todos_eventos.dto.AuthenticationDTO;
import com.todoseventos.todos_eventos.usecase.AuthService;
import com.todoseventos.todos_eventos.usecase.ClienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private ClienteService clienteService;

    @Operation(description = "Operação para logar usuario na plataforma")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login realizado com sucesso!"),
            @ApiResponse(responseCode = "417", description = "Erro ao realizar login!"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor ao realizar login!")
    })
    @PostMapping(value = "/login")
    public ResponseEntity<?> login(@RequestBody AuthenticationDTO authDto){
        return ResponseEntity.ok(authService.login(authDto));
    }
}
