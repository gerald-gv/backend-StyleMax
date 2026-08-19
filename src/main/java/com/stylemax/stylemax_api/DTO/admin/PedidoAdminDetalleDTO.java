package com.stylemax.stylemax_api.DTO.admin;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.stylemax.stylemax_api.Entity.Pedido;
import com.stylemax.stylemax_api.Enums.PedidoEstado;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PedidoAdminDetalleDTO {

    private Long id;
    private LocalDateTime fechaPedido;
    private BigDecimal total;
    private PedidoEstado estado;
    private ClientePedidoAdminDTO cliente;
    private DireccionPedidoAdminDTO direccion;
    private List<DetallePedidoAdminDTO> detalles;
    private PagoPedidoAdminDTO pago;

    public static PedidoAdminDetalleDTO fromEntity(
            Pedido pedido
    ) {

        return PedidoAdminDetalleDTO.builder()

                .id(pedido.getId())

                .fechaPedido(pedido.getFechaPedido())

                .total(pedido.getTotal())

                .estado(pedido.getEstado())

                .cliente(
                        ClientePedidoAdminDTO.builder()
                                .id(pedido.getUsuario().getId())
                                .nombre(pedido.getUsuario().getNombre())
                                .apellido(pedido.getUsuario().getApellido())
                                .correo(pedido.getUsuario().getCorreo())
                                .telefono(pedido.getUsuario().getTelefono())
                                .build()
                )

                .direccion(
                        DireccionPedidoAdminDTO.builder()
                                .departamento(pedido.getDepartamento())
                                .provincia(pedido.getProvincia())
                                .distrito(pedido.getDistrito())
                                .direccionCompleta(pedido.getDireccionCompleta())
                                .referencia(pedido.getReferencia())
                                .build()
                )

                .detalles(
                        pedido.getDetalles()
                                .stream()
                                .map(DetallePedidoAdminDTO::fromEntity)
                                .toList()
                )

                .pago(
                        PagoPedidoAdminDTO.builder()
                                .metodo("MERCADO_PAGO")
                                .preferenceId(pedido.getMercadoPagoPreferenceId())
                                .paymentId(pedido.getMercadoPagoPaymentId())
                                .build()
                )

                .build();
    }
}