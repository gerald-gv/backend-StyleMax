package com.stylemax.stylemax_api.Controller;


import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.stylemax.stylemax_api.DTO.PaginaDTO;
import com.stylemax.stylemax_api.DTO.ProductoCardDTO;
import com.stylemax.stylemax_api.DTO.ProductoDetalleDTO;
import com.stylemax.stylemax_api.Enums.Fit;
import com.stylemax.stylemax_api.Service.ProductoService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/productos")
@RequiredArgsConstructor
public class ProductoController {

    private final ProductoService productoService;

    @GetMapping
    public PaginaDTO<ProductoCardDTO> listarCatalogo(
            @RequestParam(required = false) Long categoriaId,
            @RequestParam(required = false) Long marcaId,
            @RequestParam(required = false) Fit fit,
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "0") int page) {
    	
    	if (page < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"La página no puede ser negativa");
        }
    	
        return productoService.listarCatalogo(categoriaId, marcaId, fit, q, page);
    }

    //ruta dinamica, pagina de detalle.
    @GetMapping("/{slug}")
    public ProductoDetalleDTO obtenerPorSlug(@PathVariable String slug) {
        return productoService.obtenerPorSlug(slug);
    }
    
    @GetMapping("/destacados")
    public List<ProductoCardDTO> listarDestacados() {
        return productoService.listarDestacados();
    }
}
