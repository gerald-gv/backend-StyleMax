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
    private String departamento;
    private String provincia;
    private String distrito;
    private String direccionCompleta;
    private String referencia;
    private List<DetallePedidoDTO> detalles;

    public static PedidoDTO fromEntity(Pedido pedido) {
        return PedidoDTO.builder()
                .id(pedido.getId())
                .fechaPedido(pedido.getFechaPedido())
                .total(pedido.getTotal())
                .estado(pedido.getEstado())
                .departamento(pedido.getDepartamento())
                .provincia(pedido.getProvincia())
                .distrito(pedido.getDistrito())
                .direccionCompleta(pedido.getDireccionCompleta())
                .referencia(pedido.getReferencia())
                .detalles(pedido.getDetalles().stream().map(DetallePedidoDTO::fromEntity).toList())
                .build();
    }
}
