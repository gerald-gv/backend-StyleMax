package com.stylemax.stylemax_api.DTO;

import java.time.LocalDateTime;

import com.stylemax.stylemax_api.Entity.Favorito;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FavoritoDTO {

    private Long productoId;
    private LocalDateTime fechaAgregado;
    private ProductoCardDTO producto;

    public static FavoritoDTO fromEntity(Favorito favorito) {
        return FavoritoDTO.builder()
                .productoId(favorito.getProducto().getId())
                .fechaAgregado(favorito.getFechaAgregado())
                .producto(ProductoCardDTO.fromEntity(favorito.getProducto()))
                .build();
    }
}
