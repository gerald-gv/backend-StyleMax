package com.stylemax.stylemax_api.DTO;

import com.stylemax.stylemax_api.Entity.CarritoItem;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class CarritoItemDTO {
    private Long id;
    private Long productoId;
    private String productoNombre;
    private String productoImagen;
    private Integer cantidad;
    private BigDecimal precioUnitario;
    private BigDecimal subtotal;

    public static CarritoItemDTO fromEntity(CarritoItem item) {
        return CarritoItemDTO.builder()
                .id(item.getId())
                .productoId(item.getProducto().getId())
                .productoNombre(item.getProducto().getNombre())
                .productoImagen(item.getProducto().getImagen())
                .cantidad(item.getCantidad())
                .precioUnitario(item.getPrecioUnitario())
                .subtotal(item.getPrecioUnitario().multiply(BigDecimal.valueOf(item.getCantidad())))
                .build();
    }
}
