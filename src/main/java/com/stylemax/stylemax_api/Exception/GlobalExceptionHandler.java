package com.stylemax.stylemax_api.Exception;

import com.stylemax.stylemax_api.DTO.ErrorResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorResponseDTO> handleResponseStatusException(
            ResponseStatusException ex, HttpServletRequest request) {

        HttpStatusCode statusCode = ex.getStatusCode();
        String reasonPhrase = HttpStatus.valueOf(statusCode.value()).getReasonPhrase();

        ErrorResponseDTO body = new ErrorResponseDTO(
                LocalDateTime.now(),
                statusCode.value(),
                reasonPhrase,
                ex.getReason(),
                request.getRequestURI()
        );
        return ResponseEntity.status(statusCode).body(body);
    }
}
