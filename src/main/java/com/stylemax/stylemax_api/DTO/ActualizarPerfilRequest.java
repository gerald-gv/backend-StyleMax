package com.stylemax.stylemax_api.DTO;

import jakarta.validation.constraints.NotBlank;

public record ActualizarPerfilRequest(

        @NotBlank(message = "El nombre es obligatorio")
        String nombre,

        @NotBlank(message = "El apellido es obligatorio")
        String apellido,

        String telefono

) {
}
