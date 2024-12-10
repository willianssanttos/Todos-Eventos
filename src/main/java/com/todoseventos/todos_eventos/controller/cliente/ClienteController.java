package com.todoseventos.todos_eventos.controller.cliente;

import com.todoseventos.todos_eventos.dto.cliente.ClienteRequest;
import com.todoseventos.todos_eventos.dto.cliente.ClienteResponse;
import com.todoseventos.todos_eventos.usecase.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping(value = "/api")
public class ClienteController implements IClienteController{
    @Autowired
    private ClienteService clienteService;

    @PostMapping("/cliente")
    public ResponseEntity<ClienteResponse> postPessoa(@RequestBody ClienteRequest clienteRequest) {
        return new ResponseEntity<>(clienteService.cadastrarNovaPessoa(clienteRequest), HttpStatus.CREATED);
    }

    @GetMapping("/pessoa/{identificador}")
    public ResponseEntity<ClienteResponse>getPessoa(@PathVariable String identificador) {
        if (identificador.length() == 11) { // Assumindo que CPF tem 11 dígitos
           clienteService.procurarPessoaPorCpf(identificador);
        } else if(identificador.length() == 14) { // Assumindo que CNPJ tem 14 dígitos
            clienteService.procurarPessoaPorCnpj(identificador);
        }
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/pessoa")
    public ResponseEntity<ClienteResponse> getPessoa() {
        List<ClienteResponse> response = clienteService.listarPessoas();
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PutMapping("/pessoa/{identificador}")
    public ResponseEntity<ClienteResponse> putPessoa(@PathVariable("identificador") String identificador, @RequestBody ClienteRequest clienteRequest) {
         clienteService.atualizarPessoa(identificador, clienteRequest);
         return ResponseEntity.status(HttpStatus.OK).build();
    }
}