package com.stylemax.stylemax_api.DTO.admin;


import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ClientePedidoAdminDTO {

    private Long id;
    private String nombre;
    private String apellido;
    private String correo;
    private String telefono;

}