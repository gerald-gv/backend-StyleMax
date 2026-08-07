package com.stylemax.stylemax_api.DTO;

import java.time.LocalDateTime;

public record ErrorResponseDTO (
        LocalDateTime timestamp,
        int status,
        String error,
        String message,
        String path
){
}
