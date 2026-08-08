package com.stylemax.stylemax_api.Controller;

import com.mercadopago.resources.preference.Preference;
import com.stylemax.stylemax_api.Entity.Pedido;
import com.stylemax.stylemax_api.Service.PagoService;
import com.stylemax.stylemax_api.Service.PedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/pagos")
@RequiredArgsConstructor
public class PagoController {
    private final PagoService pagoService;
    private final PedidoService pedidoService;

    @PostMapping("/pedido/{pedidoId}")
    public Map<String, String> crearPago(@PathVariable Long pedidoId) {
        Pedido pedido = pedidoService.obtenerPorId(pedidoId);
        Preference preference = pagoService.crearPreferencia(pedido);
        return Map.of(
                "preferenceId", preference.getId(),
                "initPoint", preference.getInitPoint()
        );
    }
}
