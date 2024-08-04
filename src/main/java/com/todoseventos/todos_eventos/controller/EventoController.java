package com.todoseventos.todos_eventos.controller;

import com.todoseventos.todos_eventos.dto.CustomExceptionResponse;
import com.todoseventos.todos_eventos.dto.EventoRequest;
import com.todoseventos.todos_eventos.dto.EventoResponse;
import com.todoseventos.todos_eventos.exception.CustomException;
import com.todoseventos.todos_eventos.usecase.EventoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping(value = "/api")
@CrossOrigin
public class EventoController {

    @Autowired
    private EventoService eventoService;

    @Operation(description = "Operação para cadastrado de evento")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cadastro realizado com sucesso. Seu evento já está em divulgação!"),
            @ApiResponse(responseCode = "417", description = "Erro ao cadastrar o evento!"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor ao realizar cadastro!")
    })
    @PostMapping("/evento")
    public ResponseEntity<CustomExceptionResponse> cadastrarEvento(@RequestBody EventoRequest eventoRequest) {
        try {
            EventoResponse response = eventoService.cadastrarNovoEvento(eventoRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(new CustomExceptionResponse(CustomException.CADASTRO_EVENTO, response));
        } catch (CustomException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new CustomExceptionResponse(e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new CustomExceptionResponse(CustomException.ERRO_INTERNO, null));
        }
    }

    @Operation(description = "Operação para encerrar evento")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Evento encerrado com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Erro ao encerrar evento!"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor ao encerrar evento!")
    })
    @PutMapping("/encerrar/{idEvento}")
    public ResponseEntity<CustomExceptionResponse> encerrarEvento(@PathVariable Integer idEvento) {
        try {
            EventoResponse response = eventoService.encerrarEvento(idEvento);
            return ResponseEntity.status(HttpStatus.OK).body(new CustomExceptionResponse(CustomException.EVENTO_ENCERRADO, response));
        } catch (CustomException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new CustomExceptionResponse(e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new CustomExceptionResponse(CustomException.ERRO_INTERNO, null));
        }
    }

    @Operation(description = "Operação para listar todos os eventos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de eventos recuperada com sucesso!"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor ao listar eventos!")
    })
    @GetMapping("/evento")
    public ResponseEntity<CustomExceptionResponse> listarEventos() {
        try {
            List<EventoResponse> response = eventoService.localizarEventos();
            return ResponseEntity.status(HttpStatus.OK).body(new CustomExceptionResponse(CustomException.LISTA_EVENTO, response));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new CustomExceptionResponse(CustomException.ERRO_INTERNO, null));
        }
    }

    @Operation(description = "Operação para buscar evento por nome")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Evento encontrado com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Erro ao procurar evento!"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor ao procurar evento!")
    })
    @GetMapping("/evento/{nomeEvento}")
    public ResponseEntity<CustomExceptionResponse> procurarEventoPorNome(@PathVariable String nomeEvento) {
        try {
            EventoResponse response = eventoService.procurarEventoPorNome(nomeEvento);
            return ResponseEntity.status(HttpStatus.OK).body(new CustomExceptionResponse(CustomException.EVENTO_ENCONTRADO, response));
        } catch (CustomException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new CustomExceptionResponse(e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new CustomExceptionResponse(CustomException.ERRO_INTERNO, null));
        }
    }

    @Operation(description = "Operação para atualizar evento")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Evento atualizado com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Erro ao atualizar evento!"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor ao atualizar evento!")
    })
    @PutMapping("/evento/{nomeEvento}")
    public ResponseEntity<CustomExceptionResponse> atualizarEvento(@PathVariable String nomeEvento, @RequestBody EventoRequest eventoRequest) {
        try {
            EventoResponse response = eventoService.atualizarEvento(nomeEvento, eventoRequest);
            return ResponseEntity.status(HttpStatus.OK).body(new CustomExceptionResponse(CustomException.EVENTO_ATUALIZADO, response));
        } catch (CustomException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new CustomExceptionResponse(e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new CustomExceptionResponse(CustomException.ERRO_INTERNO, null));
        }
    }

    @Operation(description = "Operação para excluir evento")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Evento excluído com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Erro ao excluir evento!"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor ao excluir evento!")
    })
    @DeleteMapping("/evento/{idEvento}")
    public ResponseEntity<CustomExceptionResponse> excluirEvento(@PathVariable Integer idEvento) {
        try {
            eventoService.excluirEvento(idEvento);
            return ResponseEntity.status(HttpStatus.OK).body(new CustomExceptionResponse(CustomException.EXCLUIR_EVENTO, null));
        } catch (CustomException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new CustomExceptionResponse(e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new CustomExceptionResponse(CustomException.ERRO_INTERNO, null));
        }
    }
}