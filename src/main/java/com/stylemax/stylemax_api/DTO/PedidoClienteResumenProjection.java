package com.stylemax.stylemax_api.DTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.stylemax.stylemax_api.Enums.PedidoEstado;

public interface PedidoClienteResumenProjection {

    Long getId();

    LocalDateTime getFechaPedido();

    PedidoEstado getEstado();

    BigDecimal getTotal();

    Long getCantidadProductos();

    String getPrimerProducto();
    
    String getPrimeraImagen();
}
