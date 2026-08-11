package com.stylemax.stylemax_api.Service;

import com.stylemax.stylemax_api.Entity.Carrito;
import com.stylemax.stylemax_api.Entity.CarritoItem;
import com.stylemax.stylemax_api.Entity.DetallePedido;
import com.stylemax.stylemax_api.Entity.Pedido;
import com.stylemax.stylemax_api.Entity.Producto;
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
import java.util.List;

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

    @Transactional
    public Pedido obtenerPorIdYUsuario(Long pedidoId, Long usuarioId) {
        Pedido pedido = obtenerPorId(pedidoId);
        if (!pedido.getUsuario().getId().equals(usuarioId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido no encontrado: " + pedidoId);
        }
        return pedido;
    }
    @Transactional
    public void guardarPreferencia(Long pedidoId, String preferenceId) {
        Pedido pedido = obtenerPorId(pedidoId);
        pedido.setMercadoPagoPreferenceId(preferenceId);
        pedidoRepository.save(pedido);
    }

    @Transactional
    public void marcarComoPagado(Long pedidoId, String paymentId) {
        Pedido pedido = obtenerPorId(pedidoId);
        pedido.setEstado(PedidoEstado.PAGADO);
        pedido.setMercadoPagoPaymentId(paymentId);
        pedidoRepository.save(pedido);
    }

    @Transactional
    public void marcarComoCancelado(Long pedidoId, String paymentId) {
        Pedido pedido = obtenerPorId(pedidoId);
        pedido.setEstado(PedidoEstado.CANCELADO);
        pedido.setMercadoPagoPaymentId(paymentId);

        for (DetallePedido detalle : pedido.getDetalles()) {
            Producto producto = detalle.getProducto();
            producto.setStock(producto.getStock() + detalle.getCantidad());
            productoRepository.save(producto);
        }
        pedidoRepository.save(pedido);
    }

    @Transactional
    public void cancelarPedidosExpirados(int minutosExpiracion) {
        LocalDateTime limite = LocalDateTime.now().minusMinutes(minutosExpiracion);
        List<Pedido> vencidos = pedidoRepository.findByEstadoAndFechaPedidoBefore(PedidoEstado.PENDIENTE, limite);

        for (Pedido pedido : vencidos) {
            marcarComoCancelado(pedido.getId(), null);
        }
    }
}
