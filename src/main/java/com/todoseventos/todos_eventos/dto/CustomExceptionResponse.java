package com.todoseventos.todos_eventos.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Builder(toBuilder = true)
public class CustomExceptionResponse {

    private String message;
    private Object data;

    public CustomExceptionResponse(String message, Object data) {

        this.message = message;
        this.data = data;
    }
}
