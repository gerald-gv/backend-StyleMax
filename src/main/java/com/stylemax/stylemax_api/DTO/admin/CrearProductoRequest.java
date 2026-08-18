package com.stylemax.stylemax_api.DTO.admin;

import java.math.BigDecimal;

import com.stylemax.stylemax_api.Enums.Fit;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CrearProductoRequest(

	    @NotBlank
	    @Size(max = 150)
	    String nombre,

	    @NotBlank
	    @Size(max = 2000)
	    String descripcion,

	    @NotNull
	    @DecimalMin("0.01")
	    BigDecimal precio,

	    @NotNull
	    @Min(0)
	    Integer stock,

	    @NotBlank
	    @Size(max = 50)
	    String color,

	    @NotNull
	    Fit fit,

	    @NotBlank
	    String imagen,

	    Boolean destacado,
	    
	    Boolean activo,

	    @NotNull
	    Long marcaId,

	    @NotNull
	    Long categoriaId

	) {
	
}