package com.todoseventos.todos_eventos.controller;

import com.todoseventos.todos_eventos.dto.EventoRequest;
import com.todoseventos.todos_eventos.dto.EventoResponse;
import com.todoseventos.todos_eventos.usecase.EventoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class EventoController {

    @Autowired
    private EventoService eventoService;

    @PostMapping
    @RequestMapping("/api/evento")
    @ResponseStatus(HttpStatus.CREATED)
    public EventoResponse cadastrarEvento(@RequestBody EventoRequest eventoRequest) {
        return eventoService.cadastrarNovoEvento(eventoRequest);
    }

    @GetMapping("/api/evento")
    @ResponseStatus(HttpStatus.OK)
    public List<EventoResponse> listarEventos() {
        return eventoService.localizarEventos();
    }

    @GetMapping("/api/evento/{nomeEvento}")
    @ResponseStatus(HttpStatus.OK)
    public EventoResponse procurarEventoPorNome(@PathVariable String nomeEvento) {
        return eventoService.procurarEventoPorNome(nomeEvento);
    }

    @PutMapping("/api/evento/{nomeEvento}")
    @ResponseStatus(HttpStatus.OK)
    public EventoResponse atualizarEvento(@PathVariable String nomeEvento, @RequestBody EventoRequest eventoRequest) {
        return eventoService.atualizarEvento(nomeEvento, eventoRequest);
    }
}
