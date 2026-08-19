package com.stylemax.stylemax_api.DTO.admin;

import com.stylemax.stylemax_api.Entity.Marca;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MarcaAdminDTO {

    private Long id;
    private String nombre;
    private Boolean activo;

    public static MarcaAdminDTO fromEntity(Marca marca) {

        return MarcaAdminDTO.builder()
                .id(marca.getId())
                .nombre(marca.getNombre())
                .activo(marca.getActivo())
                .build();
    }
}