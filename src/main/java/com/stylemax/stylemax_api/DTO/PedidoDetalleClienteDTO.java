package com.stylemax.stylemax_api.DTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.stylemax.stylemax_api.Enums.PedidoEstado;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PedidoDetalleClienteDTO {

    private Long id;
    private LocalDateTime fechaPedido;
    private PedidoEstado estado;
    private BigDecimal total;
    private List<PedidoProductoDTO> productos;
    private String departamento;
    private String provincia;
    private String distrito;
    private String direccionCompleta;
    private String referencia;
    private String metodoPago;
}
