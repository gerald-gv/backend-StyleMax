package com.stylemax.stylemax_api.Controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.stylemax.stylemax_api.DTO.FavoritoDTO;
import com.stylemax.stylemax_api.DTO.PaginaDTO;
import com.stylemax.stylemax_api.Service.FavoritoService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/favoritos")
@RequiredArgsConstructor
public class FavoritoController {

    private final FavoritoService favoritoService;

    @GetMapping
    public ResponseEntity<PaginaDTO<FavoritoDTO>> listar(
            @RequestParam(defaultValue = "0")
            int pagina,
            @RequestParam(defaultValue = "8")
            int tamanio
    ) {

        return ResponseEntity.ok(favoritoService.listar(pagina,tamanio));
    }
    @GetMapping("/{productoId}")
    public ResponseEntity<Boolean> existe(
            @PathVariable Long productoId
    ) {
        return ResponseEntity.ok(
                favoritoService.existe(productoId)
        );
    }

    @PostMapping("/{productoId}")
    public ResponseEntity<FavoritoDTO> agregar(
            @PathVariable Long productoId
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(favoritoService.agregar(productoId));
    }

    @DeleteMapping("/{productoId}")
    public ResponseEntity<Void> eliminar(
            @PathVariable Long productoId
    ) {
        favoritoService.eliminar(productoId);

        return ResponseEntity.noContent().build();
    }
}