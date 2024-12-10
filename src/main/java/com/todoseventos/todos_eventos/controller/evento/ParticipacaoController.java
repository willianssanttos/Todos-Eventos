package com.todoseventos.todos_eventos.controller.evento;

import com.todoseventos.todos_eventos.dto.ParticipacaoRequest;
import com.todoseventos.todos_eventos.dto.ParticipacaoResponse;
import com.todoseventos.todos_eventos.usecase.ParticipacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ParticipacaoController implements IParticipacaoController{

    @Autowired
    private ParticipacaoService participacaoService;

    @PostMapping("/participacao")
    public ResponseEntity<ParticipacaoResponse> inscreverParticipante(@RequestBody ParticipacaoRequest request) {
        return new ResponseEntity<>(participacaoService.inscreverParticipante(request), HttpStatus.CREATED);
    }

    @GetMapping("/confirmacao/{idParticipacao}")
    public ResponseEntity<ParticipacaoResponse> confirmarParticipacao(@PathVariable Integer idParticipacao) {
        participacaoService.confirmarParticipacao(idParticipacao);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}