package com.stylemax.stylemax_api.DTO;

import com.stylemax.stylemax_api.Entity.Usuario;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PerfilDTO {

    private Long id;
    private String nombre;
    private String apellido;
    private String correo;
    private String telefono;

    public static PerfilDTO fromEntity(Usuario usuario) {
        return PerfilDTO.builder()
                .id(usuario.getId())
                .nombre(usuario.getNombre())
                .apellido(usuario.getApellido())
                .correo(usuario.getCorreo())
                .telefono(usuario.getTelefono())
                .build();
    }
}