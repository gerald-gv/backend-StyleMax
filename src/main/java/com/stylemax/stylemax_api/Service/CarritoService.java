package com.stylemax.stylemax_api.Service;

import com.stylemax.stylemax_api.DTO.CarritoDTO;
import com.stylemax.stylemax_api.DTO.CarritoItemDTO;
import com.stylemax.stylemax_api.Entity.Carrito;
import com.stylemax.stylemax_api.Entity.CarritoItem;
import com.stylemax.stylemax_api.Entity.Producto;
import com.stylemax.stylemax_api.Entity.Usuario;
import com.stylemax.stylemax_api.Repository.CarritoItemRepository;
import com.stylemax.stylemax_api.Repository.CarritoRepository;
import com.stylemax.stylemax_api.Repository.ProductoRepository;
import com.stylemax.stylemax_api.Repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CarritoService {
    private final CarritoRepository carritoRepository;
    private final CarritoItemRepository carritoItemRepository;
    private final ProductoRepository productoRepository;
    private final UsuarioRepository usuarioRepository;

    @Transactional
    public Carrito obtenerOCrearCarrito(Long usuarioId) {
        return carritoRepository.findByUsuarioId(usuarioId)
                .orElseGet(() -> crearCarrito(usuarioId));
    }

    private Carrito crearCarrito(Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado: "+ usuarioId));
        Carrito carrito = Carrito.builder()
                .usuario(usuario)
                .fechaCreacion(LocalDateTime.now())
                .fechaActualizacion(LocalDateTime.now())
                .total(BigDecimal.ZERO)
                .build();
        return carritoRepository.save(carrito);
    }

    @Transactional
    public CarritoDTO obtenerCarrito(Long usuarioId) {
        return CarritoDTO.fromEntity(obtenerOCrearCarrito(usuarioId));
    }

    @Transactional
    public CarritoDTO agregarItem(Long usuarioId, Long productoId, Integer cantidad) {
        if (cantidad == null || cantidad <= 0) {
            throw  new ResponseStatusException(HttpStatus.BAD_REQUEST, "La cantidad debe ser mayor a 0");
        }

        Carrito carrito = obtenerOCrearCarrito(usuarioId);

        Producto producto = productoRepository.findById(productoId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado: " + productoId));

        if (!producto.getActivo()) {
            throw new ResponseStatusException(HttpStatus.GONE, "El producto " + producto.getNombre() + " ya no esta disponible");
        }

        CarritoItem itemExistente = carritoItemRepository
                .findByCarritoIdAndProductoId(carrito.getId(), productoId)
                .orElse(null);

        int cantidadFinal = (itemExistente != null ? itemExistente.getCantidad() : 0) + cantidad;
        if (cantidadFinal > producto.getStock()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Stock insuficiente. Disponible: " + producto.getStock());
        }

        if (itemExistente == null) {
            CarritoItem nuevoItem = CarritoItem.builder()
                    .carrito(carrito)
                    .producto(producto)
                    .cantidad(cantidad)
                    .precioUnitario(producto.getPrecio())
                    .build();

            carrito.getItems().add(nuevoItem);
        } else {
            itemExistente.setCantidad(cantidadFinal);
            itemExistente.setPrecioUnitario(producto.getPrecio());
        }

        recalcularTotal(carrito);
        carritoRepository.save(carrito);
        return CarritoDTO.fromEntity(carrito);
    }

    private void recalcularTotal(Carrito carrito) {
        BigDecimal total = carrito.getItems().stream()
                .map(item -> item.getPrecioUnitario().multiply(BigDecimal.valueOf(item.getCantidad())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        carrito.setTotal(total);
        carrito.setFechaActualizacion(LocalDateTime.now());
    }

    @Transactional
    public CarritoDTO actualizarCantidad(Long usuarioId, Long itemId, Integer cantidad) {
        if (cantidad == null || cantidad <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La cantidad debe ser mayor a 0");
        }

        Carrito carrito = obtenerOCrearCarrito(usuarioId);

        CarritoItem item = carrito.getItems().stream()
                .filter(i -> i.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Item no encontrado en el carrito"));

        if (cantidad > item.getProducto().getStock()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Stock insuficiente. Disponible: " + item.getProducto().getStock());
        }

        item.setCantidad(cantidad);
        recalcularTotal(carrito);
        carritoRepository.save(carrito);
        return  CarritoDTO.fromEntity(carrito);
    }

    @Transactional
    public CarritoDTO eliminarItem(Long usuarioId, Long itemId) {
        Carrito carrito = obtenerOCrearCarrito(usuarioId);

        boolean removido = carrito.getItems().removeIf(i -> i.getId().equals(itemId));

        if (!removido) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Item no encontrado en el carrito");
        }

        recalcularTotal(carrito);
        carritoRepository.save(carrito);
        return CarritoDTO.fromEntity(carrito);
    }

    @Transactional
    public void vaciarCarrito(Carrito carrito) {
        carrito.getItems().clear();
        carrito.setTotal(BigDecimal.ZERO);
        carrito.setFechaActualizacion(LocalDateTime.now());
        carritoRepository.save(carrito);
    }
}
