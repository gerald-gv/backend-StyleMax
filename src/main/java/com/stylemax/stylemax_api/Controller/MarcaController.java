package com.stylemax.stylemax_api.Controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stylemax.stylemax_api.DTO.MarcaDTO;
import com.stylemax.stylemax_api.Service.MarcaService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/marcas")
@RequiredArgsConstructor
public class MarcaController {

    private final MarcaService marcaService;

    // para filtros del catalogo
    @GetMapping
    public List<MarcaDTO> listar() {
        return marcaService.listarActivas();
    }
}
