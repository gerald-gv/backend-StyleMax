package com.stylemax.stylemax_api.DTO.admin;

import com.stylemax.stylemax_api.Entity.Categoria;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CategoriaAdminDTO {

    private Long id;
    private String nombre;
    private Boolean activo;

    public static CategoriaAdminDTO fromEntity(Categoria categoria) {

        return CategoriaAdminDTO.builder()
                .id(categoria.getId())
                .nombre(categoria.getNombre())
                .activo(categoria.getActivo())
                .build();
    }
}