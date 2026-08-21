package com.stylemax.stylemax_api.DTO;

import java.math.BigDecimal;

import com.stylemax.stylemax_api.Entity.Producto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

// Vista resumida de un producto, pensada para grillas/cards del catalogo (home, listado por categoria, destacados, resultados de busqueda).
@Getter
@Setter
@Builder
public class ProductoCardDTO {

    private Long id;
    private String nombre;
    private String slug;
    private BigDecimal precio;
    private String color;
    private String imagen;
    private Boolean destacado;
    private String marca;
    private String categoria;
    private Boolean favorito;

    public static ProductoCardDTO fromEntity(Producto p) {
        return ProductoCardDTO.builder()
                .id(p.getId())
                .nombre(p.getNombre())
                .slug(p.getSlug())
                .precio(p.getPrecio())
                .color(p.getColor())
                .imagen(p.getImagen())
                .destacado(p.getDestacado())
                .marca(p.getMarca().getNombre())
                .categoria(p.getCategoria().getNombre())
                .favorito(false)
                .build();
    }
}
