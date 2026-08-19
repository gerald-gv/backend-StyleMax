package com.stylemax.stylemax_api.Service;

import java.util.Locale;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.stylemax.stylemax_api.DTO.PaginaDTO;
import com.stylemax.stylemax_api.DTO.admin.ActualizarPedidoEstadoAdminRequest;
import com.stylemax.stylemax_api.DTO.admin.PedidoAdminDTO;
import com.stylemax.stylemax_api.DTO.admin.PedidoAdminDetalleDTO;
import com.stylemax.stylemax_api.DTO.admin.PedidoEstadisticasDTO;
import com.stylemax.stylemax_api.Entity.Pedido;
import com.stylemax.stylemax_api.Enums.PedidoEstado;
import com.stylemax.stylemax_api.Repository.PedidoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminPedidoService {

    private static final int TAMANIO_PAGINA_ADMIN = 20;

    private final PedidoRepository pedidoRepository;

    @Transactional(readOnly = true)
    public PaginaDTO<PedidoAdminDTO> listarPedidos(int pagina,String estado,String q) {

        if (pagina < 0) {
            pagina = 0;
        }

        Pageable pageable = PageRequest.of(pagina,TAMANIO_PAGINA_ADMIN,
                Sort.by(
                        Sort.Direction.DESC,
                        "fechaPedido"
                )
        );

        PedidoEstado estadoFiltro = estado != null && !estado.isBlank() ? convertirEstado(estado) : null;

        String busqueda = q != null && !q.isBlank() ? q.trim() : null;

        Page<Pedido> pedidos;

        if (busqueda != null) {

            pedidos = pedidoRepository.buscar(busqueda, estadoFiltro, pageable);

        } else if (estadoFiltro != null) {

            pedidos = pedidoRepository.findByEstado(estadoFiltro, pageable);

        } else {
            pedidos = pedidoRepository.findAll(pageable);
        }

        return construirPagina(pedidos);
    }

    private PedidoEstado convertirEstado(String estado) {

        try {

            return PedidoEstado.valueOf(estado.trim().toUpperCase(Locale.ROOT));

        } catch (IllegalArgumentException e) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,  "Estado de pedido inválido: " + estado);
        }
    }
    
    @Transactional(readOnly = true)
    public PedidoEstadisticasDTO obtenerEstadisticas() {

        return PedidoEstadisticasDTO.builder()
                .pendientes(pedidoRepository.countByEstado(PedidoEstado.PENDIENTE))
                .pagados(pedidoRepository.countByEstado(PedidoEstado.PAGADO))
                .empaquetando(pedidoRepository.countByEstado(PedidoEstado.EMPAQUETANDO))
                .enviando(pedidoRepository.countByEstado(PedidoEstado.ENVIANDO))
                .entregados(pedidoRepository.countByEstado(PedidoEstado.ENTREGADO))
                .cancelados(pedidoRepository.countByEstado(PedidoEstado.CANCELADO))
                .build();
    }
    
    @Transactional(readOnly = true)
    public PedidoAdminDetalleDTO obtenerDetalle(Long id) {

        Pedido pedido = pedidoRepository.buscarDetalleAdmin(id)
        		.orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND,"Pedido no encontrado")
                );

        return PedidoAdminDetalleDTO.fromEntity(pedido);
    }
    
    @Transactional
    public PedidoAdminDetalleDTO actualizarEstado(Long id,ActualizarPedidoEstadoAdminRequest request) {

        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND,"Pedido no encontrado")
                );

        PedidoEstado estadoActual = pedido.getEstado();
        PedidoEstado nuevoEstado = request.getEstado();

        validarTransicion(estadoActual,nuevoEstado);

        pedido.setEstado(nuevoEstado);
        pedido = pedidoRepository.save(pedido);

        return PedidoAdminDetalleDTO.fromEntity(pedido);
    }

    private PaginaDTO<PedidoAdminDTO> construirPagina(Page<Pedido> pagina) {

        return PaginaDTO.<PedidoAdminDTO>builder()

                .contenido(
                        pagina.getContent()
                                .stream()
                                .map(PedidoAdminDTO::fromEntity)
                                .toList()
                )

                .pagina(pagina.getNumber())
                .tamanio(pagina.getSize())
                .totalElementos(pagina.getTotalElements())
                .totalPaginas(pagina.getTotalPages())
                .ultima(pagina.isLast())

                .build();
    }
    
    private void validarTransicion(PedidoEstado actual,PedidoEstado nuevo) {

        boolean valida = switch (actual) {

            case PENDIENTE ->
                    nuevo == PedidoEstado.CANCELADO;

            case PAGADO ->
                    nuevo == PedidoEstado.EMPAQUETANDO;

            case EMPAQUETANDO ->
                    nuevo == PedidoEstado.ENVIANDO;

            case ENVIANDO ->
                    nuevo == PedidoEstado.ENTREGADO;

            case ENTREGADO,
                 CANCELADO ->
                    false;
        };

        if (!valida) {

            throw new ResponseStatusException(HttpStatus.CONFLICT, "No se puede cambiar el pedido de " + actual + " a " + nuevo
            );
        }
    }
}