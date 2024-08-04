package com.todoseventos.todos_eventos.controller;

import com.todoseventos.todos_eventos.dto.CustomExceptionResponse;
import com.todoseventos.todos_eventos.exception.CustomException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExcecoesController {

    @Operation(description = "Tratamento para exceções personalizadas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "Erro de negócio personalizado!")
    })
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<CustomExceptionResponse> handleCustomException(CustomException ex) {
        CustomExceptionResponse response = new CustomExceptionResponse(ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @Operation(description = "Tratamento para exceções genéricas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor!")
    })
    @ExceptionHandler(Exception.class)
    public ResponseEntity<CustomExceptionResponse> handleGenericException(Exception ex) {
        CustomExceptionResponse response = new CustomExceptionResponse(CustomException.ERRO_INTERNO, null);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}


