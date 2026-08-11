package com.stylemax.stylemax_api.DTO;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest (
        @NotBlank String correo,
        @NotBlank String password
){
}
