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
import com.stylemax.stylemax_api.DTO.admin.ActualizarMarcaRequest;
import com.stylemax.stylemax_api.DTO.admin.CrearMarcaRequest;
import com.stylemax.stylemax_api.DTO.admin.MarcaAdminDTO;
import com.stylemax.stylemax_api.DTO.admin.MarcaEstadisticasDTO;
import com.stylemax.stylemax_api.Service.MarcaService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin/marcas")
@PreAuthorize("hasRole('ADMINISTRADOR')")
@RequiredArgsConstructor
public class AdminMarcaController {

    private final MarcaService marcaService;

    @GetMapping
    public PaginaDTO<MarcaAdminDTO> listar(@RequestParam(required = false) String q, @RequestParam(defaultValue = "0") int page) {

        if (page < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"La página no puede ser negativa");
        }

        return marcaService.listarParaAdmin(q, page);
    }

    @GetMapping("/estadisticas")
    public MarcaEstadisticasDTO obtenerEstadisticas() {
        return marcaService.obtenerEstadisticas();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MarcaAdminDTO crear(@Valid @RequestBody CrearMarcaRequest request) {
        return marcaService.crear(request);
    }

    @PutMapping("/{id}")
    public MarcaAdminDTO actualizar(@PathVariable Long id,@Valid @RequestBody ActualizarMarcaRequest request) {
        return marcaService.actualizar(id, request);
    }

    @DeleteMapping("/{id}")
    public MarcaAdminDTO eliminar(@PathVariable Long id) {
        return marcaService.eliminar(id);
    }
}
