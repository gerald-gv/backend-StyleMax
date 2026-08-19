package com.stylemax.stylemax_api.DTO.admin;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CrearMarcaRequest(
        @NotBlank
        @Size(max = 100)
        String nombre,

        Boolean activo
) {}