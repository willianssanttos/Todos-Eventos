package com.todoseventos.todos_eventos.controller;

import com.todoseventos.todos_eventos.dto.ApiResponse;
import com.todoseventos.todos_eventos.dto.ClienteRequest;
import com.todoseventos.todos_eventos.dto.ClienteResponse;
import com.todoseventos.todos_eventos.exception.CustomException;
import com.todoseventos.todos_eventos.usecase.ClienteService;
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

    @PostMapping("/pessoa")
    public ResponseEntity<ApiResponse> postPessoa(@RequestBody ClienteRequest clienteRequest) {
        try {
            ClienteResponse response = clienteService.cadastrarNovaPessoa(clienteRequest);
            return ResponseEntity.ok(new ApiResponse("Cadastro realizado com sucesso!", response));
        } catch (CustomException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Erro interno ao realizar cadastro", null));
        }
    }

    @GetMapping("/pessoa/{identificador}")
    public ResponseEntity<ApiResponse> getPessoa(@PathVariable String identificador) {
        try {
            ClienteResponse pessoa;
            if (identificador.length() == 11) { // Assumindo que CPF tem 11 dígitos
                pessoa = clienteService.procurarPessoaPorCpf(identificador);
            } else if (identificador.length() == 14) { // Assumindo que CNPJ tem 14 dígitos
                pessoa = clienteService.procurarPessoaPorCnpj(identificador);
            } else {
                throw new CustomException(CustomException.IDENTIFICADOR_INVALIDO);
            }
            return ResponseEntity.ok(new ApiResponse("Cliente encontrado!", pessoa));
        } catch (CustomException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Erro interno ao buscar cliente", null));
        }
    }

    @GetMapping("/pessoa")
    public ResponseEntity<ApiResponse> getPessoa() {
        try {
            List<ClienteResponse> response = clienteService.listarPessoas();
            return ResponseEntity.ok(new ApiResponse("Lista de clientes recuperada com sucesso!", response));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Erro interno ao listar clientes", null));
        }
    }

    @PutMapping("/pessoa/{identificador}")
    public ResponseEntity<ApiResponse> putPessoa(@PathVariable("identificador") String identificador, @RequestBody ClienteRequest clienteRequest) {
        try {
            ClienteResponse response = clienteService.atualizarPessoa(identificador, clienteRequest);
            return ResponseEntity.ok(new ApiResponse("Cliente atualizado com sucesso!", response));
        } catch (CustomException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Erro interno ao atualizar cliente", null));
        }
    }
}