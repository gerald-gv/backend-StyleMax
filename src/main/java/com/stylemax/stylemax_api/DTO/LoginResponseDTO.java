package com.stylemax.stylemax_api.DTO;

public record LoginResponseDTO(
        String token,
        Long usuarioId,
        String nombre,
        String correo,
        String rol
) {
}
