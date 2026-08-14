package com.stylemax.stylemax_api.Controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stylemax.stylemax_api.DTO.ActualizarDireccionRequest;
import com.stylemax.stylemax_api.DTO.DireccionDTO;
import com.stylemax.stylemax_api.Service.DireccionService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/direccion")
@RequiredArgsConstructor
public class DireccionController {

    private final DireccionService direccionService;

    @GetMapping
    public DireccionDTO obtenerDireccion(@AuthenticationPrincipal Long usuarioId) {
        return direccionService.obtenerDireccion(usuarioId);
    }


    @PutMapping
    public DireccionDTO guardarDireccion(@AuthenticationPrincipal Long usuarioId,@Valid @RequestBody ActualizarDireccionRequest request) {
        return direccionService.guardarDireccion(usuarioId,request);
    }
}