package com.stylemax.stylemax_api.Controller;



import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.stylemax.stylemax_api.DTO.PaginaDTO;
import com.stylemax.stylemax_api.DTO.admin.ActualizarProductoRequest;
import com.stylemax.stylemax_api.DTO.admin.CrearProductoRequest;
import com.stylemax.stylemax_api.DTO.admin.ProductoAdminDTO;
import com.stylemax.stylemax_api.DTO.admin.ProductoEstadisticasDTO;
import com.stylemax.stylemax_api.Service.ProductoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin/productos")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMINISTRADOR')")
public class AdminProductoController {

    private final ProductoService productoService;

    @GetMapping
    public PaginaDTO<ProductoAdminDTO> listarProductos(@RequestParam(required = false) String q, @RequestParam(defaultValue = "0") int page) {

        if (page < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"La página no puede ser negativa");
        }

        return productoService.listarParaAdmin(q, page);
    }
    
    @GetMapping("/estadisticas")
    public ProductoEstadisticasDTO obtenerEstadisticas() {
        return productoService.obtenerEstadisticas();
    }


    @GetMapping("/{id}")
    public ProductoAdminDTO obtenerPorId(@PathVariable Long id) {
        return productoService.obtenerPorIdParaAdmin(id);
    }


    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public ProductoAdminDTO crear(@Valid @RequestBody CrearProductoRequest request) {
        return productoService.crear(request);
    }


    @PutMapping("/{id}")
    public ProductoAdminDTO actualizar(@PathVariable Long id,@Valid @RequestBody ActualizarProductoRequest request) {
        return productoService.actualizar(id, request);
    }


    @DeleteMapping("/{id}")
    public ProductoAdminDTO eliminar(@PathVariable Long id) {
        return productoService.eliminar(id);
    }
}