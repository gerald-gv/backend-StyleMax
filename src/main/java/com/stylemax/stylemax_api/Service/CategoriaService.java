package com.stylemax.stylemax_api.Service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.stylemax.stylemax_api.DTO.CategoriaDTO;
import com.stylemax.stylemax_api.Repository.CategoriaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    // Categorias activas, para la seccion "Categorias Clasicas" del home.
    public List<CategoriaDTO> listarActivas() {
        return categoriaRepository.findByActivoTrueOrderByNombreAsc()
                .stream()
                .map(CategoriaDTO::fromEntity)
                .toList();
    }
}
