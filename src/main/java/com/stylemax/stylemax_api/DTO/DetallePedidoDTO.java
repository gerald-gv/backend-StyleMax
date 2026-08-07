package com.stylemax.stylemax_api.DTO;

import com.stylemax.stylemax_api.Entity.DetallePedido;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class DetallePedidoDTO {
    private Long productoId;
    private String productoNombre;
    private Integer cantidad;
    private BigDecimal precioUnitario;
    private BigDecimal subtotal;

    public static DetallePedidoDTO fromEntity(DetallePedido detalle) {
        return DetallePedidoDTO.builder()
                .productoId(detalle.getProducto().getId())
                .productoNombre(detalle.getProducto().getNombre())
                .cantidad(detalle.getCantidad())
                .subtotal(detalle.getPrecioUnitario().multiply(BigDecimal.valueOf(detalle.getCantidad())))
                .build();
    }
}
