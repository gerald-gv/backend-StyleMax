package com.stylemax.stylemax_api.DTO.admin;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PedidoEstadisticasDTO {

    private long pendientes;
    private long pagados;
    private long empaquetando;
    private long enviando;
    private long entregados;
    private long cancelados;
}