package com.stylemax.stylemax_api.Controller;

import com.stylemax.stylemax_api.DTO.PedidoDTO;
import com.stylemax.stylemax_api.Entity.Pedido;
import com.stylemax.stylemax_api.Service.PedidoService;
import lombok.RequiredArgsConstructor;
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

    @GetMapping("/{pedidoId}")
    public PedidoDTO obtener(@AuthenticationPrincipal Long usuarioId, @PathVariable Long pedidoId) {
        Pedido pedido = pedidoService.obtenerPorIdYUsuario(pedidoId, usuarioId);
        return PedidoDTO.fromEntity(pedido);
    }
}
