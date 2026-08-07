package com.stylemax.stylemax_api.DTO;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record ActualizarCantidadRequest(
        @NotNull @Min(1) Integer cantidad
) {
}
