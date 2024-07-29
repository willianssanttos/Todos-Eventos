package com.todoseventos.todos_eventos.controller;

import com.todoseventos.todos_eventos.dto.ApiResponse;
import com.todoseventos.todos_eventos.dto.ParticipacaoRequest;
import com.todoseventos.todos_eventos.dto.ParticipacaoResponse;
import com.todoseventos.todos_eventos.usecase.ParticipacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ParticipacaoController {

    @Autowired
    private ParticipacaoService participacaoService;

    @PostMapping
    @RequestMapping("/api/participacao")
    public ResponseEntity<ApiResponse> inscreverParticipante(@RequestBody ParticipacaoRequest request) {
        try {
            ParticipacaoResponse response = participacaoService.inscreverParticipante(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse("Inscrição realizada com sucesso!", response));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse("Erro ao realizar inscrição: " + e.getMessage(), null));
        }
    }

    @GetMapping("/api/confirmacao/{idParticipacao}")
    public ResponseEntity<ApiResponse> confirmarParticipacao(@PathVariable Long idParticipacao) {
        try {
            ParticipacaoResponse response = participacaoService.confirmarParticipacao(idParticipacao);
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Participação confirmada com sucesso!", response));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse("Erro ao confirmar participação: " + e.getMessage(), null));
        }
    }
}