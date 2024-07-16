package com.todoseventos.todos_eventos.controller;

import com.todoseventos.todos_eventos.dto.PessoaRequest;
import com.todoseventos.todos_eventos.dto.PessoaResponse;
import com.todoseventos.todos_eventos.usecase.PessoaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pessoa")
public class PessoaController {

    @Autowired
    private PessoaService pessoaService;

    @PostMapping
    public ResponseEntity<PessoaResponse> postPessoa(@RequestBody PessoaRequest pessoaRequest) {
        PessoaResponse response = pessoaService.cadastrarNovaPessoa(pessoaRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/fisica/{cpf}")
    public ResponseEntity<PessoaResponse> getPessoaFisica(@PathVariable("cpf") String cpf) {
        try {
            PessoaResponse response = pessoaService.procurarPessoaPorCpf(cpf);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("/juridica/{cnpj}")
    public ResponseEntity<PessoaResponse> getPessoaJuridica(@PathVariable("cnpj") String cnpj) {
        try {
            PessoaResponse response = pessoaService.procurarPessoaPorCnpj(cnpj);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<PessoaResponse> putPessoa(@PathVariable("id") Long idPessoa,
                                                    @RequestBody PessoaRequest pessoaRequest) {
        try {
            PessoaResponse response = pessoaService.atualizarPessoa(idPessoa, pessoaRequest);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}
