package com.stylemax.stylemax_api.DTO.admin;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DireccionPedidoAdminDTO {

    private String departamento;
    private String provincia;
    private String distrito;
    private String direccionCompleta;
    private String referencia;
}