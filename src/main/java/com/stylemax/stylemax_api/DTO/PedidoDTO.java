package com.stylemax.stylemax_api.DTO;

import com.stylemax.stylemax_api.Entity.Pedido;
import com.stylemax.stylemax_api.Enums.PedidoEstado;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class PedidoDTO {
    private Long id;
    private LocalDateTime fechaPedido;
    private BigDecimal total;
    private PedidoEstado estado;
    private List<DetallePedidoDTO> detalles;

    public static PedidoDTO fromEntity(Pedido pedido) {
        return PedidoDTO.builder()
                .id(pedido.getId())
                .fechaPedido(pedido.getFechaPedido())
                .total(pedido.getTotal())
                .estado(pedido.getEstado())
                .detalles(pedido.getDetalles().stream().map(DetallePedidoDTO::fromEntity).toList())
                .build();
    }
}
