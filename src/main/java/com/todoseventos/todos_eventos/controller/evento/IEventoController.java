package com.todoseventos.todos_eventos.controller.evento;

import com.todoseventos.todos_eventos.dto.EventoRequest;
import com.todoseventos.todos_eventos.dto.EventoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

public interface IEventoController {

    @Operation(description = "Operação para cadastrado de evento")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cadastro realizado com sucesso. Seu evento já está em divulgação!"),
            @ApiResponse(responseCode = "417", description = "Erro ao cadastrar o evento!"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor ao realizar cadastro!")
    })
    ResponseEntity<EventoResponse> cadastrarEvento(@RequestBody EventoRequest eventoRequest);

    @Operation(description = "Operação para encerrar evento")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Evento encerrado com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Erro ao encerrar evento!"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor ao encerrar evento!")
    })
    ResponseEntity<EventoResponse> encerrarEvento(@PathVariable Integer idEvento);

    @Operation(description = "Operação para listar todos os eventos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de eventos recuperada com sucesso!"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor ao listar eventos!")
    })
    ResponseEntity<EventoResponse> listarEventos();

    @Operation(description = "Operação para buscar evento por nome")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Evento encontrado com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Erro ao procurar evento!"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor ao procurar evento!")
    })
    ResponseEntity<EventoResponse> procurarEventoPorNome(@PathVariable String nomeEvento);

    @Operation(description = "Operação para atualizar evento")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Evento atualizado com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Erro ao atualizar evento!"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor ao atualizar evento!")
    })
    ResponseEntity<EventoResponse> atualizarEvento(@PathVariable String nomeEvento, @RequestBody EventoRequest eventoRequest);

    @Operation(description = "Operação para excluir evento")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Evento excluído com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Erro ao excluir evento!"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor ao excluir evento!")
    })
    ResponseEntity<Void> excluirEvento(@PathVariable Integer idEvento);
}
