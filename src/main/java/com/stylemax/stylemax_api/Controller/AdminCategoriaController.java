package com.stylemax.stylemax_api.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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
import com.stylemax.stylemax_api.DTO.admin.ActualizarCategoriaRequest;
import com.stylemax.stylemax_api.DTO.admin.CategoriaAdminDTO;
import com.stylemax.stylemax_api.DTO.admin.CategoriaEstadisticasDTO;
import com.stylemax.stylemax_api.DTO.admin.CrearCategoriaRequest;
import com.stylemax.stylemax_api.Service.CategoriaService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin/categorias")
@RequiredArgsConstructor
public class AdminCategoriaController {
	
	private final CategoriaService categoriaService;


    @GetMapping
    public PaginaDTO<CategoriaAdminDTO> listar(@RequestParam(required = false) String q, @RequestParam(defaultValue = "0") int page) {

        if (page < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"La página no puede ser negativa");
        }

        return categoriaService.listarParaAdmin(q, page);
    }


    @GetMapping("/estadisticas")
    public CategoriaEstadisticasDTO obtenerEstadisticas() {

        return categoriaService.obtenerEstadisticas();
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoriaAdminDTO crear(@Valid @RequestBody CrearCategoriaRequest request) {

        return categoriaService.crear(request);
    }


    @PutMapping("/{id}")
    public CategoriaAdminDTO actualizar(@PathVariable Long id, @Valid @RequestBody ActualizarCategoriaRequest request) {

        return categoriaService.actualizar(id, request);
    }
    
    @DeleteMapping("/{id}")
    public CategoriaAdminDTO eliminar(
            @PathVariable Long id
    ) {
        return categoriaService.eliminar(id);
    }
}
