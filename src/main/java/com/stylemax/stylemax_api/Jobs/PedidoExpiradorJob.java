package com.stylemax.stylemax_api.Jobs;

import com.stylemax.stylemax_api.Service.PedidoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PedidoExpiradorJob {

    private final PedidoService pedidoService;

    @Value("${pedido.expiracion-minutos}")
    private int minutosExpiracion;

    @Scheduled(fixedRateString = "${pedido.expiracion-check-rate-ms}")
    public void revisarPedidosExpirados() {
        log.info("Revisando pedidos PENDIENTE con mas de {} minutos...", minutosExpiracion);
        pedidoService.cancelarPedidosExpirados(minutosExpiracion);
    }
}
// 300000 15