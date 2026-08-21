package com.stylemax.stylemax_api.DTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.stylemax.stylemax_api.Enums.PedidoEstado;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PedidoClienteResumenDTO {

    private Long id;
    private LocalDateTime fechaPedido;
    private PedidoEstado estado;
    private BigDecimal total;
    private Long cantidadProductos;
    private String primerProducto;
    private String primeraImagen;
}