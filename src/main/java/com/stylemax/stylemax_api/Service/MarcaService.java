package com.stylemax.stylemax_api.Service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.stylemax.stylemax_api.DTO.MarcaDTO;
import com.stylemax.stylemax_api.Repository.MarcaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MarcaService {

    private final MarcaRepository marcaRepository;

    // Marcas activas, para filtros del catalogo
    public List<MarcaDTO> listarActivas() {
        return marcaRepository.findByActivoTrueOrderByNombreAsc()
                .stream()
                .map(MarcaDTO::fromEntity)
                .toList();
    }
}
