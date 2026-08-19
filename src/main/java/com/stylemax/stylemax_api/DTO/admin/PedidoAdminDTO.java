package com.stylemax.stylemax_api.DTO.admin;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.stylemax.stylemax_api.Entity.Pedido;
import com.stylemax.stylemax_api.Enums.PedidoEstado;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PedidoAdminDTO {

    private Long id;
    private String cliente;
    private String correo;
    private LocalDateTime fechaPedido;
    private BigDecimal total;
    private PedidoEstado estado;

    public static PedidoAdminDTO fromEntity(Pedido pedido) {

        return PedidoAdminDTO.builder()
                .id(pedido.getId())
                .cliente(pedido.getUsuario().getNombre() + " " + pedido.getUsuario().getApellido())
                .correo( pedido.getUsuario().getCorreo())
                .fechaPedido(pedido.getFechaPedido())
                .total(pedido.getTotal())
                .estado(pedido.getEstado())
                .build();
    }
}