package com.stylemax.stylemax_api.Controller;

import com.stylemax.stylemax_api.DTO.CarritoDTO;
import com.stylemax.stylemax_api.DTO.ActualizarCantidadRequest;
import com.stylemax.stylemax_api.DTO.AgregarItemCarritoRequest;
import com.stylemax.stylemax_api.Service.CarritoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

// TODO(seguridad): usuarioId viaja como query param porque todavia no hay JWT.
/* Cuando exista autenticacion, este valor DEBE salir del usuario autenticado
 (SecurityContext), nunca de un parametro que manda el cliente.*/

@RestController
@RequestMapping("/api/carrito")
@RequiredArgsConstructor
public class CarritoController {
    private final CarritoService carritoService;

    @GetMapping
    public CarritoDTO obtenerCarrito(@RequestParam Long usuarioId) {
        return carritoService.obtenerCarrito(usuarioId);
    }

    @PostMapping("/items")
    public CarritoDTO agregarItem(@RequestParam Long usuarioId, @Valid @RequestBody AgregarItemCarritoRequest request) {
        return carritoService.agregarItem(usuarioId, request.productoId(), request.cantidad());
    }

    @PutMapping("/items/{itemId}")
    public CarritoDTO actualizarCantidad(@RequestParam Long usuarioId, @PathVariable Long itemId, @Valid @RequestBody ActualizarCantidadRequest request) {
        return carritoService.actualizarCantidad(usuarioId, itemId, request.cantidad());
    }
    @DeleteMapping("/items/{itemId}")
    public CarritoDTO eliminarItem(@RequestParam Long usuarioId, @PathVariable Long itemId) {
        return carritoService.eliminarItem(usuarioId, itemId);
    }
}
