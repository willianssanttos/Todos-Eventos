package com.todoseventos.todos_eventos.controller;

import com.todoseventos.todos_eventos.dto.CustomExceptionResponse;
import com.todoseventos.todos_eventos.dto.ClienteRequest;
import com.todoseventos.todos_eventos.dto.ClienteResponse;
import com.todoseventos.todos_eventos.exception.CustomException;
import com.todoseventos.todos_eventos.usecase.ClienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping(value = "/api")
@CrossOrigin
public class ClienteController {
    @Autowired
    private ClienteService clienteService;

    @Operation(description = "Operação para cadastrar clientes")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cadastro realizado com sucesso!"),
            @ApiResponse(responseCode = "417", description = "Erro ao realizar o cadastro!"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor ao realizar cadastro!")
    })
    @PostMapping("/cliente")
    public ResponseEntity<CustomExceptionResponse> postPessoa(@RequestBody ClienteRequest clienteRequest) {
        try {
            ClienteResponse response = clienteService.cadastrarNovaPessoa(clienteRequest);
            return ResponseEntity.ok(new CustomExceptionResponse(CustomException.CADASTRO_CLIENTE, null));
        } catch (CustomException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new CustomExceptionResponse(e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new CustomExceptionResponse(CustomException.ERRO_INTERNO, null));
        }
    }

    @Operation(description = "Operação para buscar cliente por CPF ou CNPJ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente encontrado!"),
            @ApiResponse(responseCode = "400", description = "Identificador inválido!"),
            @ApiResponse(responseCode = "500", description = "Erro interno ao buscar cliente!")
    })
    @GetMapping("/pessoa/{identificador}")
    public ResponseEntity<CustomExceptionResponse> getPessoa(@PathVariable String identificador) {
        try {
            ClienteResponse pessoa;
            if (identificador.length() == 11) { // Assumindo que CPF tem 11 dígitos
                pessoa = clienteService.procurarPessoaPorCpf(identificador);
            } else if (identificador.length() == 14) { // Assumindo que CNPJ tem 14 dígitos
                pessoa = clienteService.procurarPessoaPorCnpj(identificador);
            } else {
                throw new CustomException(CustomException.IDENTIFICADOR_INVALIDO);
            }
            return ResponseEntity.ok(new CustomExceptionResponse(CustomException.CLIENTE_ENCONTRADO, pessoa));
        } catch (CustomException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new CustomExceptionResponse(e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new CustomExceptionResponse(CustomException.ERRO_INTERNO, null));
        }
    }

    @Operation(description = "Operação para listar todos os clientes")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de clientes recuperada com sucesso!"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor ao listar clientes!")
    })
    @GetMapping("/pessoa")
    public ResponseEntity<CustomExceptionResponse> getPessoa() {
        try {
            List<ClienteResponse> response = clienteService.listarPessoas();
            return ResponseEntity.ok(new CustomExceptionResponse(CustomException.LISTA_CLIENTE, response));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new CustomExceptionResponse(CustomException.ERRO_INTERNO, null));
        }
    }

    @Operation(description = "Operação para atualizar cliente por CPF ou CNPJ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente atualizado com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Erro ao atualizar cliente!"),
            @ApiResponse(responseCode = "500", description = "Erro interno ao atualizar cliente!")
    })
    @PutMapping("/pessoa/{identificador}")
    public ResponseEntity<CustomExceptionResponse> putPessoa(@PathVariable("identificador") String identificador, @RequestBody ClienteRequest clienteRequest) {
        try {
            ClienteResponse response = clienteService.atualizarPessoa(identificador, clienteRequest);
            return ResponseEntity.ok(new CustomExceptionResponse(CustomException.CLIENTE_ATUALIZADO, response));
        } catch (CustomException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new CustomExceptionResponse(e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new CustomExceptionResponse(CustomException.ERRO_INTERNO, null));
        }
    }
}