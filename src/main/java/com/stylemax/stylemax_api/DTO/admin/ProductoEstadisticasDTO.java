package com.stylemax.stylemax_api.DTO.admin;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProductoEstadisticasDTO {

    private Long total;
    private Long activos;
    private Long sinStock;
    private Long destacados;

    public static ProductoEstadisticasDTO fromProjection(
            ProductosEstadisticasProjection projection
    ) {
        return ProductoEstadisticasDTO.builder()
                .total(projection.getTotal())
                .activos(projection.getActivos())
                .sinStock(projection.getSinStock())
                .destacados(projection.getDestacados())
                .build();
    }
}