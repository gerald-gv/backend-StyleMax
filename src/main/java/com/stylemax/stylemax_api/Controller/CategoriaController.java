package com.stylemax.stylemax_api.Controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stylemax.stylemax_api.DTO.CategoriaDTO;
import com.stylemax.stylemax_api.Service.CategoriaService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/categorias")
@RequiredArgsConstructor
public class CategoriaController {

    private final CategoriaService categoriaService;

    //para la seccion "Categorias Clasicas" del home.
    @GetMapping
    public List<CategoriaDTO> listar() {
        return categoriaService.listarActivas();
    }
}
