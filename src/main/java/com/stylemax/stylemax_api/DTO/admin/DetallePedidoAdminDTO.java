package com.stylemax.stylemax_api.DTO.admin;

import java.math.BigDecimal;

import com.stylemax.stylemax_api.Entity.DetallePedido;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DetallePedidoAdminDTO {

    private Long productoId;
    private String productoNombre;
    private Integer cantidad;
    private BigDecimal precioUnitario;
    private BigDecimal subtotal;

    public static DetallePedidoAdminDTO fromEntity(
            DetallePedido detalle
    ) {

        BigDecimal subtotal = detalle.getPrecioUnitario().multiply(
                                BigDecimal.valueOf(
                                        detalle.getCantidad()
                                )
                        );

        return DetallePedidoAdminDTO.builder()
                .productoId(detalle.getProducto().getId())
                .productoNombre( detalle.getProducto().getNombre())
                .cantidad(detalle.getCantidad())
                .precioUnitario(detalle.getPrecioUnitario())
                .subtotal(subtotal)
                .build();
    }
}