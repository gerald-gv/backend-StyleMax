package com.stylemax.stylemax_api.DTO;

import com.stylemax.stylemax_api.Entity.Carrito;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Builder
public class CarritoDTO {
    private Long id;
    private BigDecimal total;
    private List<CarritoItemDTO> items;

    public static CarritoDTO fromEntity(Carrito carrito) {
        return CarritoDTO.builder()
                .id(carrito.getId())
                .total(carrito.getTotal())
                .items(carrito.getItems().stream().map(CarritoItemDTO::fromEntity).toList())
                .build();
    }
}
