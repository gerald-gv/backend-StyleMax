package com.stylemax.stylemax_api.DTO;

import java.math.BigDecimal;

import com.stylemax.stylemax_api.Entity.Producto;
import com.stylemax.stylemax_api.Enums.Fit;

import lombok.Builder;
import lombok.Getter;

// Vista completa de un producto, usada en la pagina de detalle (ruta dinamica GET /api/productos/{slug}).
@Getter
@Builder
public class ProductoDetalleDTO {

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
    private MarcaDTO marca;
    private CategoriaDTO categoria;

    public static ProductoDetalleDTO fromEntity(Producto p) {
        return ProductoDetalleDTO.builder()
                .id(p.getId())
                .nombre(p.getNombre())
                .slug(p.getSlug())
                .descripcion(p.getDescripcion())
                .precio(p.getPrecio())
                .stock(p.getStock())
                .color(p.getColor())
                .fit(p.getFit())
                .imagen(p.getImagen())
                .destacado(p.getDestacado())
                .marca(MarcaDTO.fromEntity(p.getMarca()))
                .categoria(CategoriaDTO.fromEntity(p.getCategoria()))
                .build();
    }
}
