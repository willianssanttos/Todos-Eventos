package com.todoseventos.todos_eventos.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Builder(toBuilder = true)
public class ApiResponse {

    private String message;
    private Object data;

    public ApiResponse(String message, Object data) {

        this.message = message;
        this.data = data;
    }
}
