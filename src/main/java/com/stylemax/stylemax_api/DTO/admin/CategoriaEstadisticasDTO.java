package com.stylemax.stylemax_api.DTO.admin;

import lombok.Builder;

@Builder
public record CategoriaEstadisticasDTO(
        Long total,
        Long activas,
        Long inactivas
) {

    public static CategoriaEstadisticasDTO fromProjection(
            CategoriaEstadisticasProjection projection
    ) {

        return CategoriaEstadisticasDTO.builder()
                .total(projection.getTotal())
                .activas(projection.getActivas())
                .inactivas(projection.getInactivas())
                .build();
    }
}