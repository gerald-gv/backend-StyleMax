package com.stylemax.stylemax_api.DTO.admin;

import com.stylemax.stylemax_api.Enums.PedidoEstado;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ActualizarPedidoEstadoAdminRequest {

    private PedidoEstado estado;
}