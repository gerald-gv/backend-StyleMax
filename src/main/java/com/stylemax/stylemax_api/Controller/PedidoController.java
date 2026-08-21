package com.stylemax.stylemax_api.Controller;

import com.stylemax.stylemax_api.DTO.PaginaDTO;
import com.stylemax.stylemax_api.DTO.PedidoClienteResumenDTO;
import com.stylemax.stylemax_api.DTO.PedidoDTO;
import com.stylemax.stylemax_api.DTO.PedidoDetalleClienteDTO;
import com.stylemax.stylemax_api.Entity.Pedido;
import com.stylemax.stylemax_api.Enums.PedidoEstado;
import com.stylemax.stylemax_api.Service.PedidoService;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/pedidos")
@RequiredArgsConstructor
public class PedidoController {
    private final PedidoService pedidoService;

    @PostMapping("/checkout")
    public PedidoDTO checkout(@AuthenticationPrincipal Long usuarioId) {
        Pedido pedido = pedidoService.crearPedidoDesdeCarrito(usuarioId);
        return PedidoDTO.fromEntity(pedido);
    }
    
    @GetMapping("/mis-pedidos")
    public PaginaDTO<PedidoClienteResumenDTO> obtenerMisPedidos(
            @AuthenticationPrincipal Long usuarioId,
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "10") int tamanio,
            @RequestParam(required = false) List<PedidoEstado> estados
    ) {

        return pedidoService.obtenerPedidosCliente(
                usuarioId,
                pagina,
                tamanio,
                estados
        );

    }
    
    @GetMapping("/mis-pedidos/{pedidoId}")
    public PedidoDetalleClienteDTO obtenerDetallePedido( @AuthenticationPrincipal Long usuarioId, @PathVariable Long pedidoId) {

        return pedidoService.obtenerDetalleCliente( pedidoId, usuarioId);
    }

    @GetMapping("/{pedidoId}")
    public PedidoDTO obtener(@AuthenticationPrincipal Long usuarioId, @PathVariable Long pedidoId) {
        Pedido pedido = pedidoService.obtenerPorIdYUsuario(pedidoId, usuarioId);
        return PedidoDTO.fromEntity(pedido);
    }
    

}
