package com.todoseventos.todos_eventos.controller.evento;

import com.todoseventos.todos_eventos.dto.EventoRequest;
import com.todoseventos.todos_eventos.dto.EventoResponse;
import com.todoseventos.todos_eventos.usecase.EventoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping(value = "/api")
public class EventoController implements IEventoController{

    @Autowired
    private EventoService eventoService;

    @PostMapping("/evento")
    public ResponseEntity<EventoResponse> cadastrarEvento(@RequestBody EventoRequest eventoRequest) {
           return new ResponseEntity<>(eventoService.cadastrarNovoEvento(eventoRequest), HttpStatus.CREATED);
    }

    @PutMapping("/encerrar/{idEvento}")
    public ResponseEntity<EventoResponse> encerrarEvento(@PathVariable Integer idEvento) {
        eventoService.encerrarEvento(idEvento);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/evento")
    public ResponseEntity<EventoResponse> listarEventos() {
        List<EventoResponse> response = eventoService.localizarEventos();
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/evento/{nomeEvento}")
    public ResponseEntity<EventoResponse> procurarEventoPorNome(@PathVariable String nomeEvento) {
       eventoService.procurarEventoPorNome(nomeEvento);
       return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PutMapping("/evento/{nomeEvento}")
    public ResponseEntity<EventoResponse> atualizarEvento(@PathVariable String nomeEvento, @RequestBody EventoRequest eventoRequest) {
        eventoService.atualizarEvento(nomeEvento, eventoRequest);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/evento/{idEvento}")
    public ResponseEntity<Void> excluirEvento(@PathVariable Integer idEvento) {
      eventoService.excluirEvento(idEvento);
      return ResponseEntity.status(HttpStatus.OK).build();
    }
}