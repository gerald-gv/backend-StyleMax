package com.stylemax.stylemax_api.DTO.admin;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UsuarioEstadisticasDTO {

    private long totalUsuarios;

    private long administradores;

    private long clientes;

    private long usuariosActivos;

    private long usuariosInactivos;

}