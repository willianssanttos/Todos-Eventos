package com.todoseventos.todos_eventos.controller;

import com.todoseventos.todos_eventos.dto.ApiResponse;
import com.todoseventos.todos_eventos.dto.ParticipacaoRequest;
import com.todoseventos.todos_eventos.dto.ParticipacaoResponse;
import com.todoseventos.todos_eventos.usecase.ParticipacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/participacao")
public class ParticipacaoController {

    @Autowired
    private ParticipacaoService participacaoService;

    @PostMapping
    public ResponseEntity<ApiResponse> inscreverParticipante(@RequestBody ParticipacaoRequest request) {
        try {
            ParticipacaoResponse response = participacaoService.inscreverParticipante(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse("Inscrição realizada com sucesso!", response));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse("Erro ao realizar inscrição: " + e.getMessage(), null));
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponse> listarParticipacoes() {
        try {
            List<ParticipacaoResponse> response = participacaoService.listarParticipacoes();
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Lista de participações recuperada com sucesso!", response));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse("Erro ao listar participações: " + e.getMessage(), null));
        }
    }

    @PutMapping("/{idParticipacao}")
    public ResponseEntity<ApiResponse> atualizarStatusParticipacao(@PathVariable Long idParticipacao, @RequestParam String status) {
        try {
            ParticipacaoResponse response = participacaoService.atualizarStatusParticipacao(idParticipacao, status);
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Status de participação atualizado com sucesso!", response));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse("Erro ao atualizar status de participação: " + e.getMessage(), null));
        }
    }
}