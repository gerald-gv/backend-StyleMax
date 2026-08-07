package com.stylemax.stylemax_api.Controller;

import com.stylemax.stylemax_api.DTO.PedidoDTO;
import com.stylemax.stylemax_api.Entity.Pedido;
import com.stylemax.stylemax_api.Service.PedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pedidos")
@RequiredArgsConstructor
public class PedidoController {
    private final PedidoService pedidoService;

    // Carrito -> Pedido Pendiente
    @PostMapping("/checkout")
    public PedidoDTO checkout(@RequestParam Long usuarioId) {
        Pedido pedido = pedidoService.crearPedidoDesdeCarrito(usuarioId);
        return PedidoDTO.fromEntity(pedido);
    }

    @GetMapping("/{pedidoId}")
    public PedidoDTO obtener(@PathVariable Long pedidoId) {
        return PedidoDTO.fromEntity(pedidoService.obtenerPorId(pedidoId));
    }
}
