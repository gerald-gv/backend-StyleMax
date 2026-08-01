package com.stylemax.stylemax_api.DTO;

import com.stylemax.stylemax_api.Entity.Categoria;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CategoriaDTO {

    private Long id;
    private String nombre;

    public static CategoriaDTO fromEntity(Categoria c) {
        return CategoriaDTO.builder()
                .id(c.getId())
                .nombre(c.getNombre())
                .build();
    }
}
