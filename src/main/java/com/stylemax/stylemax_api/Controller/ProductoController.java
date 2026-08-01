package com.stylemax.stylemax_api.Controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.stylemax.stylemax_api.DTO.ProductoCardDTO;
import com.stylemax.stylemax_api.DTO.ProductoDetalleDTO;
import com.stylemax.stylemax_api.Service.ProductoService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/productos")
@RequiredArgsConstructor
public class ProductoController {

    private final ProductoService productoService;

    @GetMapping
    public List<ProductoCardDTO> listarCatalogo(
            @RequestParam(required = false) Long categoriaId,
            @RequestParam(required = false) Long marcaId,
            @RequestParam(required = false) String q) {
        return productoService.listarCatalogo(categoriaId, marcaId, q);
    }

    //ruta dinamica, pagina de detalle.
    @GetMapping("/{slug}")
    public ProductoDetalleDTO obtenerPorSlug(@PathVariable String slug) {
        return productoService.obtenerPorSlug(slug);
    }
}
