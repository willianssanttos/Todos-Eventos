package com.todoseventos.todos_eventos.controller;


import com.todoseventos.todos_eventos.dto.ApiResponse;
import com.todoseventos.todos_eventos.dto.EventoRequest;
import com.todoseventos.todos_eventos.dto.EventoResponse;
import com.todoseventos.todos_eventos.usecase.EventoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class EventoController {

    @Autowired
    private EventoService eventoService;

    @PostMapping("/api/evento")
    public ResponseEntity<ApiResponse> cadastrarEvento(@RequestBody EventoRequest eventoRequest) {
        try {
            EventoResponse response = eventoService.cadastrarNovoEvento(eventoRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse("“Cadastro realizado com sucesso. Seu evento já está em divulgação!", response));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse("Erro ao cadastrar evento: " + e.getMessage(), null));
        }
    }

    @PutMapping("/encerrar/{idEvento}")
    public ResponseEntity<ApiResponse> encerrarEvento(@PathVariable Long idEvento) {
        try {
            EventoResponse response = eventoService.encerrarEvento(idEvento);
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Evento encerrado com sucesso!", response));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse("Erro ao encerrar evento: " + e.getMessage(), null));
        }
    }

    @GetMapping("/api/evento")
    public ResponseEntity<ApiResponse> listarEventos() {
        try {
            List<EventoResponse> response = eventoService.localizarEventos();
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Lista de eventos recuperada com sucesso!", response));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse("Erro ao listar eventos: " + e.getMessage(), null));
        }
    }

    @GetMapping("/api/evento/{nomeEvento}")
    public ResponseEntity<ApiResponse> procurarEventoPorNome(@PathVariable String nomeEvento) {
        try {
            EventoResponse response = eventoService.procurarEventoPorNome(nomeEvento);
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Evento encontrado com sucesso!", response));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse("Erro ao procurar evento: " + e.getMessage(), null));
        }
    }

    @PutMapping("/api/evento/{nomeEvento}")
    public ResponseEntity<ApiResponse> atualizarEvento(@PathVariable String nomeEvento, @RequestBody EventoRequest eventoRequest) {
        try {
            EventoResponse response = eventoService.atualizarEvento(nomeEvento, eventoRequest);
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Evento atualizado com sucesso!", response));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse("Erro ao atualizar evento, nome errado,: " + e.getMessage(), null));
        }
    }

    @DeleteMapping("/api/evento/{idEvento}")
    public ResponseEntity<ApiResponse> excluirEvento(@PathVariable Long idEvento) {
        try {
            eventoService.excluirEvento(idEvento);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new ApiResponse("Evento excluído com sucesso!", excluirEvento(idEvento)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse("Erro ao excluir evento: " + e.getMessage(), null));
        }
    }
}
