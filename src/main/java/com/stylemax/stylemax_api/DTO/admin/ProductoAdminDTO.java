package com.stylemax.stylemax_api.DTO.admin;

import java.math.BigDecimal;

import com.stylemax.stylemax_api.Enums.Fit;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProductoAdminDTO {

    private Long id;
    private String nombre;
    private String slug;
    private String descripcion;
    private BigDecimal precio;
    private Integer stock;
    private String color;
    private Fit fit;
    private String imagen;
    private Boolean destacado;
    private Boolean activo;

    private Long marcaId;
    private String marca;

    private Long categoriaId;
    private String categoria;
}