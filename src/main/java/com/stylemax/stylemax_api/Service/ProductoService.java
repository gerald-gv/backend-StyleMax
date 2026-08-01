package com.stylemax.stylemax_api.Service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.stylemax.stylemax_api.DTO.ProductoCardDTO;
import com.stylemax.stylemax_api.DTO.ProductoDetalleDTO;
import com.stylemax.stylemax_api.Entity.Producto;
import com.stylemax.stylemax_api.Repository.ProductoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductoService {

    private final ProductoRepository productoRepository;

    //Catalogo principal
    public List<ProductoCardDTO> listarCatalogo(Long categoriaId, Long marcaId, String q) {
        String query = (q == null || q.isBlank()) ? null : q.trim();
        return productoRepository.buscarCatalogo(categoriaId, marcaId, query)
                .stream()
                .map(ProductoCardDTO::fromEntity)
                .toList();
    }
    
    // Detalle de producto para la ruta dinamica
    public ProductoDetalleDTO obtenerPorSlug(String slug) {
        Producto producto = productoRepository.findBySlug(slug)
                .filter(Producto::getActivo)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Producto no encontrado: " + slug));
        return ProductoDetalleDTO.fromEntity(producto);
    }
}
