package com.stylemax.stylemax_api.Controller;

import com.mercadopago.resources.preference.Preference;
import com.stylemax.stylemax_api.Entity.Pedido;
import com.stylemax.stylemax_api.Service.PagoService;
import com.stylemax.stylemax_api.Service.PedidoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/pagos")
@RequiredArgsConstructor
public class PagoController {
    private final PagoService pagoService;
    private final PedidoService pedidoService;

    @PostMapping("/pedido/{pedidoId}")
    public Map<String, String> crearPago(@AuthenticationPrincipal Long usuarioId, @PathVariable Long pedidoId) {
        Pedido pedido = pedidoService.obtenerPorIdYUsuario(pedidoId, usuarioId);
        Preference preference = pagoService.crearPreferencia(pedido);
        return Map.of(
                "preferenceId", preference.getId(),
                "initPoint", preference.getInitPoint()
        );
    }

    @PostMapping("/webhook")
    public void webhook(@RequestParam(required = false) String topic,
                        @RequestParam(required = false) String type,
                        @RequestParam(name = "data.id", required = false) String dataIdQuery,
                        @RequestParam(name = "id", required = false) String legacyId,
                        @RequestBody(required = false)Map<String, Object> body) {

        log.info("Webhook MP recibido - query: topic={}, type={}, data.id={}, id={} | body: {}",
                topic, type, dataIdQuery, legacyId, body);

        String tipo = type != null ? type : topic;
        String paymentId = dataIdQuery != null ? dataIdQuery : legacyId;

        //Si no vino nada util por query params, probamos leerlo del body JSON

        if (paymentId == null && body != null) {
            tipo = (String) body.get("type");
            Object data = body.get("data");
            if (data instanceof Map<?, ?> dataMap) {
                Object id = dataMap.get("id");
                paymentId = id != null ? id.toString() : null;
            }
        }

        pagoService.procesarNotificacion(tipo, paymentId);
    }
}
