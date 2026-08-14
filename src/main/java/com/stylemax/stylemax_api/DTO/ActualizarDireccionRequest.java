package com.stylemax.stylemax_api.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ActualizarDireccionRequest (

        @NotBlank(message = "El departamento es obligatorio")
        @Size(max = 100, message = "El departamento no puede superar los 100 caracteres")
        String departamento,

        @NotBlank(message = "La provincia es obligatoria")
        @Size(max = 100, message = "La provincia no puede superar los 100 caracteres")
        String provincia,

        @NotBlank(message = "El distrito es obligatorio")
        @Size(max = 100, message = "El distrito no puede superar los 100 caracteres")
        String distrito,

        @NotBlank(message = "La dirección es obligatoria")
        @Size(max = 200, message = "La dirección no puede superar los 200 caracteres")
        String direccionCompleta,

        @Size(max = 200, message = "La referencia no puede superar los 200 caracteres")
        String referencia

) {
}
