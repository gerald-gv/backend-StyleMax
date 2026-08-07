package com.stylemax.stylemax_api.Service;

import com.stylemax.stylemax_api.Entity.*;
import com.stylemax.stylemax_api.Enums.PedidoEstado;
import com.stylemax.stylemax_api.Repository.PedidoRepository;
import com.stylemax.stylemax_api.Repository.ProductoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Comparator;

@Service
@RequiredArgsConstructor
public class PedidoService {
    private final PedidoRepository pedidoRepository;
    private final ProductoRepository productoRepository;
    private final CarritoService  carritoService;

    @Transactional
    public Pedido crearPedidoDesdeCarrito(Long usuarioId) {
        Carrito carrito = carritoService.obtenerOCrearCarrito(usuarioId);

        if (carrito.getItems().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El carrito es vacío");
        }

        Pedido pedido = Pedido.builder()
                .usuario(carrito.getUsuario())
                .fechaPedido(LocalDateTime.now())
                .estado(PedidoEstado.PENDIENTE)
                .total(carrito.getTotal())
                .build();

        // Orden fijo por producto id para evitar un dead lock
        carrito.getItems().stream()
                .sorted(Comparator.comparing(item -> item.getProducto().getId()))
                .forEach(item -> reservarStockYAgregarDetalle(pedido, item));

        Pedido pedidoGuardado = pedidoRepository.save(pedido);
        carritoService.vaciarCarrito(carrito);
        return pedidoGuardado;
    }

    private void reservarStockYAgregarDetalle(Pedido pedido, CarritoItem item) {
        Producto producto = productoRepository
                .buscarConLockParaActualizarStock(item.getProducto().getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado"));

        if (!producto.getActivo()) {
            throw new ResponseStatusException(HttpStatus.GONE, "El producto " + producto.getNombre() + " ya no esta disponible");
        }

        if (item.getCantidad() > producto.getStock()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Stock insuficiente para " + producto.getNombre());
        }

        producto.setStock(producto.getStock() - item.getCantidad());
        productoRepository.save(producto);

        DetallePedido detalle = DetallePedido.builder()
                .pedido(pedido)
                .producto(producto)
                .cantidad(item.getCantidad())
                .precioUnitario(item.getPrecioUnitario())
                .build();
        pedido.getDetalles().add(detalle);
    }

    @Transactional
    public Pedido obtenerPorId(Long pedidoId) {
        return pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No se ha encontrado el pedido: " + pedidoId));
    }
}
