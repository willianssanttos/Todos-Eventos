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
public class ClienteController {

    @Autowired
    private ClienteService clienteService;
    @PostMapping
    @RequestMapping("/api/pessoa")
    public ResponseEntity<ApiResponse> postPessoa(@RequestBody ClienteRequest clienteRequest) {
        ClienteResponse response = clienteService.cadastrarNovaPessoa(clienteRequest);
        return ResponseEntity.ok(new ApiResponse("Cadastro realizado com sucesso!", null));
    }

    @GetMapping("/api/pessoa/{identificador}")
    public ResponseEntity<ApiResponse> getPessoa(@PathVariable String identificador) {
        try {
            ClienteResponse pessoa;
            if (identificador.length() == 11) { // Assumindo que CPF tem 11 dígitos
                pessoa = clienteService.procurarPessoaPorCpf(identificador);
            } else if (identificador.length() == 14) { // Assumindo que CNPJ tem 14 dígitos
                pessoa = clienteService.procurarPessoaPorCnpj(identificador);
            } else {
                throw new CustomException("Identificador inválido!");
            }
            return ResponseEntity.ok(new ApiResponse("Cliente encontrado!",pessoa));
        } catch (CustomException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(e.getMessage(),null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Cadastro não localizado", null));
        }
    }

    @GetMapping("/api/pessoa")
    public ResponseEntity<ApiResponse> getPessoa() {
        List<ClienteResponse> response = clienteService.listarPessoas();
        return ResponseEntity.ok(new ApiResponse("Lista de cliente!", response));
    }

    @PutMapping("/api/pessoa/{identificador}")
    public ResponseEntity<ApiResponse> putPessoa(@PathVariable("identificador") String identificador, @RequestBody ClienteRequest clienteRequest) {
        try {
            ClienteResponse response = clienteService.atualizarPessoa(identificador, clienteRequest);
            return ResponseEntity.ok(new ApiResponse("Cliente atualizado com sucesso!", response));
        } catch (CustomException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Cliente não encontrado!", null));
        }
    }
}
