package com.stylemax.stylemax_api.DTO.admin;

public interface ProductosEstadisticasProjection {
	Long getTotal();

    Long getActivos();

    Long getSinStock();

    Long getDestacados();
}
