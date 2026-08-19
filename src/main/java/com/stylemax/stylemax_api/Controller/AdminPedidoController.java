package com.stylemax.stylemax_api.Controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.stylemax.stylemax_api.DTO.PaginaDTO;
import com.stylemax.stylemax_api.DTO.admin.ActualizarPedidoEstadoAdminRequest;
import com.stylemax.stylemax_api.DTO.admin.PedidoAdminDTO;
import com.stylemax.stylemax_api.DTO.admin.PedidoAdminDetalleDTO;
import com.stylemax.stylemax_api.DTO.admin.PedidoEstadisticasDTO;
import com.stylemax.stylemax_api.Service.AdminPedidoService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin/pedidos")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMINISTRADOR')")
public class AdminPedidoController {

    private final AdminPedidoService adminPedidoService;

    @GetMapping
    public PaginaDTO<PedidoAdminDTO> listarPedidos(
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(required = false) String estado,
            @RequestParam(required = false) String q
    ) {

        return adminPedidoService.listarPedidos(
                pagina,
                estado,
                q
        );
    }
    
    @GetMapping("/{id}")
    public PedidoAdminDetalleDTO obtenerDetalle(
            @PathVariable Long id
    ) {

        return adminPedidoService.obtenerDetalle(id);
    }
    
    
    @PatchMapping("/{id}/estado")
    public PedidoAdminDetalleDTO actualizarEstado(
            @PathVariable Long id,
            @RequestBody ActualizarPedidoEstadoAdminRequest request
    ) {

        return adminPedidoService.actualizarEstado(
                id,
                request
        );
    }
    
    
    @GetMapping("/estadisticas")
    public PedidoEstadisticasDTO obtenerEstadisticas() {

        return adminPedidoService.obtenerEstadisticas();
    }
    
    
}