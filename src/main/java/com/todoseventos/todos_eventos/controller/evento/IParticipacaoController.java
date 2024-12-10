package com.todoseventos.todos_eventos.controller.evento;

import com.todoseventos.todos_eventos.dto.ParticipacaoRequest;
import com.todoseventos.todos_eventos.dto.ParticipacaoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

public interface IParticipacaoController {

    @Operation(description = "Operação para cadastrar participante no evento")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Inscrição realizada com sucesso!"),
            @ApiResponse(responseCode = "417", description = "Erro ao cadastrar o participante!"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor ao realizar cadastro!")
    })
    ResponseEntity<ParticipacaoResponse> inscreverParticipante(@RequestBody ParticipacaoRequest request);

    @Operation(description = "Operação para confirmar participação no evento")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Participação no evento confirmada com sucesso!"),
            @ApiResponse(responseCode = "417", description = "Erro ao confirmar participação!"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor ao confirmar participação!")
    })
    ResponseEntity<ParticipacaoResponse> confirmarParticipacao(@PathVariable Integer idParticipacao);
}
