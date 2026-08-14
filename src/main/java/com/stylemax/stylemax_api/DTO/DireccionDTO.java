package com.stylemax.stylemax_api.DTO;

import com.stylemax.stylemax_api.Entity.Direccion;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DireccionDTO {

    private Long id;
    private Long usuarioId;

    private String departamento;
    private String provincia;
    private String distrito;
    private String direccionCompleta;
    private String referencia;


    public static DireccionDTO fromEntity(Direccion direccion) {

        return DireccionDTO.builder()
                .id(direccion.getId())
                .usuarioId(direccion.getUsuario().getId())
                .departamento(direccion.getDepartamento())
                .provincia(direccion.getProvincia())
                .distrito(direccion.getDistrito())
                .direccionCompleta(direccion.getDireccionCompleta())
                .referencia(direccion.getReferencia())
                .build();
    }
}