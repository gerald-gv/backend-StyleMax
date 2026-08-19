package com.stylemax.stylemax_api.DTO.admin;

import com.stylemax.stylemax_api.DTO.DireccionDTO;
import com.stylemax.stylemax_api.Entity.Direccion;
import com.stylemax.stylemax_api.Entity.Usuario;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UsuarioAdminDTO {

    private Long id;

    private String nombre;

    private String apellido;

    private String correo;

    private String telefono;

    private Boolean activo;

    private Long rolId;

    private String rol;
    
    private DireccionDTO direccion;

    public static UsuarioAdminDTO fromEntity(Usuario usuario, Direccion direccion) {

        return UsuarioAdminDTO.builder()
                .id(usuario.getId())
                .nombre(usuario.getNombre())
                .apellido(usuario.getApellido())
                .correo(usuario.getCorreo())
                .telefono(usuario.getTelefono())
                .activo(usuario.getActivo())
                .rolId(usuario.getRol().getId())
                .rol(usuario.getRol().getNombre())
                .direccion(
                        direccion != null
                                ? DireccionDTO.fromEntity(direccion)
                                : null
                )
                .build();
    }
}