package com.stylemax.stylemax_api.Service;

import com.mercadopago.client.preference.PreferenceBackUrlsRequest; // URLs de retorno (success/pending/failure).
import com.mercadopago.client.preference.PreferenceClient; // Es el objeto que hace la llamada a la API de Mercado Pago
import com.mercadopago.client.preference.PreferenceItemRequest; // Item individual de la preferencia (un producto)
import com.mercadopago.client.preference.PreferenceRequest; // Lo que se pide a mercado pago como preferencia (los ítems, las URLs, etc.)

import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;

//Se usa como tipo de retorno | Contiene la preferencia ya creada (id, link de pago (init point))
import com.mercadopago.resources.preference.Preference;

import com.stylemax.stylemax_api.Entity.DetallePedido;
import com.stylemax.stylemax_api.Entity.Pedido;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PagoService {

    private final PedidoService pedidoService;

    @Value("${app.frontend-url}")
    private String frontendUrl;

    @Value("${app.backend-url}")
    private String backendUrl;

    // La preferencia de pago se crea a partir ded un Pedido existente en la bd
    public Preference crearPreferencia(Pedido pedido) {
        try {
            List<PreferenceItemRequest> items = pedido.getDetalles().stream()
                    .map(this::toPreferenceItem)
                    .toList();

            PreferenceRequest request = PreferenceRequest.builder()
                    .items(items)
                    .externalReference(pedido.getId().toString())
                    .backUrls(PreferenceBackUrlsRequest.builder()
                            .success(frontendUrl + "/checkout/success")
                            .pending(frontendUrl + "/checkout/pending")
                            .failure(frontendUrl + "/checkout/failure")
                            .build())
                    //.autoReturn("approved")
                    // ============================================================
                    // ATENCION: autoReturn("approved") DESACTIVADO A PROPOSITO.
                    // Mercado Pago exige que back_urls.success sea una URL PUBLICA
                    // valida para poder usar auto_return con localhost, la API
                    // rechaza la preferencia entera con "back_url.success must be
                    // defined".
                    // Reactivar esta linea cuando frontendUrl deje de ser localhost
                    // (tunel o deploy real). Sin esto, el usuario ve un boton
                    // "Volver al sitio" en vez de ser redirigido automaticamente.
                    // ============================================================
                    .notificationUrl(backendUrl + "/api/pagos/webhook")
                    .build();

            Preference preference = new PreferenceClient().create(request);
            pedidoService.guardarPreferencia(pedido.getId(), preference.getId());
            return preference;
        } catch (MPApiException e) {
            log.error("Mercado Pago rechazo la preferencia: {}", e.getApiResponse().getContent());
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "No se pudo generar el pago");
        } catch (MPException e) {
            log.error("Error de conexion con Mercado Pago", e);
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "No se pudo generar el pago");
        }
    }

    private PreferenceItemRequest toPreferenceItem(DetallePedido detalle) {
        return PreferenceItemRequest.builder()
                .id(detalle.getProducto().getId().toString())
                .title(detalle.getProducto().getNombre())
                .quantity(detalle.getCantidad())
                .unitPrice(detalle.getPrecioUnitario())
                .currencyId("PEN")
                .build();
    }
}
