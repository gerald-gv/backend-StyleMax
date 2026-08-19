package com.stylemax.stylemax_api.DTO.admin;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MarcaEstadisticasDTO {

    private Long total;
    private Long activas;
    private Long inactivas;

    public static MarcaEstadisticasDTO fromProjection(
            MarcaEstadisticasProjection projection
    ) {
        return MarcaEstadisticasDTO.builder()
                .total(projection.getTotal())
                .activas(projection.getActivas())
                .inactivas(projection.getInactivas())
                .build();
    }
}
