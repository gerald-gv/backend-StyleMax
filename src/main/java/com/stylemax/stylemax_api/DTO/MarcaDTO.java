package com.stylemax.stylemax_api.DTO;

import com.stylemax.stylemax_api.Entity.Marca;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MarcaDTO {

    private Long id;
    private String nombre;

    public static MarcaDTO fromEntity(Marca m) {
        return MarcaDTO.builder()
                .id(m.getId())
                .nombre(m.getNombre())
                .build();
    }
}
