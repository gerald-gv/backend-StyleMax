package com.stylemax.stylemax_api.Controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stylemax.stylemax_api.DTO.ActualizarPerfilRequest;
import com.stylemax.stylemax_api.DTO.PerfilDTO;
import com.stylemax.stylemax_api.Service.PerfilService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/perfil")
@RequiredArgsConstructor
public class PerfilController {

    private final PerfilService perfilService;

    @GetMapping
    public PerfilDTO obtenerPerfil(@AuthenticationPrincipal Long usuarioId) {
        return perfilService.obtenerPerfil(usuarioId);
    }

    @PutMapping
    public PerfilDTO actualizarPerfil(@AuthenticationPrincipal Long usuarioId, @Valid @RequestBody ActualizarPerfilRequest request) {
        return perfilService.actualizarPerfil(usuarioId, request);
    }
}