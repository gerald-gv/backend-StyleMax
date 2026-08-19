package com.stylemax.stylemax_api.DTO.admin;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PagoPedidoAdminDTO {

    private String metodo;

    private String preferenceId;

    private String paymentId;
}