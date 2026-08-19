package com.stylemax.stylemax_api.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.stylemax.stylemax_api.DTO.PaginaDTO;
import com.stylemax.stylemax_api.DTO.admin.ActualizarUsuarioAdminRequest;
import com.stylemax.stylemax_api.DTO.admin.RestablecerPasswordAdminRequest;
import com.stylemax.stylemax_api.DTO.admin.UsuarioAdminDTO;
import com.stylemax.stylemax_api.DTO.admin.UsuarioEstadisticasDTO;
import com.stylemax.stylemax_api.Service.AdminUsuarioService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin/usuarios")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMINISTRADOR')")
@Validated
public class AdminUsuarioController {

    private final AdminUsuarioService adminUsuarioService;

    @GetMapping
    public PaginaDTO<UsuarioAdminDTO> listarUsuarios(@RequestParam(defaultValue = "0") int pagina,
    		@RequestParam(required = false) String rol, @RequestParam(required = false) String q) {

        return adminUsuarioService.listarUsuarios(pagina,rol,q);
    }

    @GetMapping("/estadisticas")
    public UsuarioEstadisticasDTO obtenerEstadisticas() {
        return adminUsuarioService.obtenerEstadisticas();
    }

    @GetMapping("/{id}")
    public UsuarioAdminDTO obtenerUsuario(@PathVariable Long id) {
        return adminUsuarioService.obtenerUsuario(id);
    }

    @PutMapping("/{id}")
    public UsuarioAdminDTO actualizarUsuario(@PathVariable Long id, @Valid @RequestBody ActualizarUsuarioAdminRequest request) {

        return adminUsuarioService.actualizarUsuario(id,request);
    }

    @PutMapping("/{id}/password")
    public ResponseEntity<Void> restablecerPassword(@PathVariable Long id,@Valid @RequestBody RestablecerPasswordAdminRequest request) {
        adminUsuarioService.restablecerPassword(id,request);
        return ResponseEntity.noContent().build();
    }
    
    @PutMapping("/{id}/activar")
    public UsuarioAdminDTO activarUsuario(@PathVariable Long id) {
        return adminUsuarioService.activarUsuario(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Long id) {
        adminUsuarioService.eliminarUsuario(id);
        return ResponseEntity.noContent().build();
    }
}