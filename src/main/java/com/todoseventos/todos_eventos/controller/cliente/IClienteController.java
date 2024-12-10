package com.todoseventos.todos_eventos.controller.cliente;

import com.todoseventos.todos_eventos.dto.cliente.ClienteRequest;
import com.todoseventos.todos_eventos.dto.cliente.ClienteResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

public interface IClienteController {
    @Operation(description = "Operação para cadastrar clientes")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cadastro realizado com sucesso!"),
            @ApiResponse(responseCode = "417", description = "Erro ao realizar o cadastro!"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor ao realizar cadastro!")
    })
    ResponseEntity<ClienteResponse> postPessoa(@RequestBody ClienteRequest clienteRequest);

    @Operation(description = "Operação para buscar cliente por CPF ou CNPJ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente encontrado!"),
            @ApiResponse(responseCode = "400", description = "Identificador inválido!"),
            @ApiResponse(responseCode = "500", description = "Erro interno ao buscar cliente!")
    })
    ResponseEntity<ClienteResponse> getPessoa(@PathVariable String identificador);

    @Operation(description = "Operação para listar todos os clientes")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de clientes recuperada com sucesso!"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor ao listar clientes!")
    })
    ResponseEntity<ClienteResponse> getPessoa();

    @Operation(description = "Operação para atualizar cliente por CPF ou CNPJ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente atualizado com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Erro ao atualizar cliente!"),
            @ApiResponse(responseCode = "500", description = "Erro interno ao atualizar cliente!")
    })
    ResponseEntity<ClienteResponse> putPessoa(@PathVariable("identificador") String identificador, @RequestBody ClienteRequest clienteRequest);
}
