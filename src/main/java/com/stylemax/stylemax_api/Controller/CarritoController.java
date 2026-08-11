package com.stylemax.stylemax_api.Controller;

import com.stylemax.stylemax_api.DTO.CarritoDTO;
import com.stylemax.stylemax_api.DTO.ActualizarCantidadRequest;
import com.stylemax.stylemax_api.DTO.AgregarItemCarritoRequest;
import com.stylemax.stylemax_api.Service.CarritoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/carrito")
@RequiredArgsConstructor
public class CarritoController {
    private final CarritoService carritoService;

    @GetMapping
    public CarritoDTO obtenerCarrito(@AuthenticationPrincipal Long usuarioId) {
        return carritoService.obtenerCarrito(usuarioId);
    }

    @PostMapping("/items")
    public CarritoDTO agregarItem(@AuthenticationPrincipal Long usuarioId,
                                  @Valid @RequestBody AgregarItemCarritoRequest request) {
        return carritoService.agregarItem(usuarioId, request.productoId(), request.cantidad());
    }

    @PutMapping("/items/{itemId}")
    public CarritoDTO actualizarCantidad(@AuthenticationPrincipal Long usuarioId,
                                         @PathVariable Long itemId,
                                         @Valid @RequestBody ActualizarCantidadRequest request) {
        return carritoService.actualizarCantidad(usuarioId, itemId, request.cantidad());
    }
    @DeleteMapping("/items/{itemId}")
    public CarritoDTO eliminarItem(@AuthenticationPrincipal Long usuarioId, @PathVariable Long itemId) {
        return carritoService.eliminarItem(usuarioId, itemId);
    }
}
