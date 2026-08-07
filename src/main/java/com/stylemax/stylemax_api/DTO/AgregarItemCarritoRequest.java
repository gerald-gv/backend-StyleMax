package com.stylemax.stylemax_api.DTO;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record AgregarItemCarritoRequest(
        @NotNull Long productoId,
        @NotNull @Min(1) Integer cantidad
) {
}
