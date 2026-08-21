package com.stylemax.stylemax_api.Service;

import java.time.LocalDateTime;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.stylemax.stylemax_api.DTO.FavoritoDTO;
import com.stylemax.stylemax_api.DTO.PaginaDTO;
import com.stylemax.stylemax_api.Entity.Favorito;
import com.stylemax.stylemax_api.Entity.Producto;
import com.stylemax.stylemax_api.Entity.Usuario;
import com.stylemax.stylemax_api.Repository.FavoritoRepository;
import com.stylemax.stylemax_api.Repository.ProductoRepository;
import com.stylemax.stylemax_api.Repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FavoritoService {

    private final FavoritoRepository favoritoRepository;
    private final UsuarioRepository usuarioRepository;
    private final ProductoRepository productoRepository;

    @Transactional
    public FavoritoDTO agregar(Long productoId) {

        Long usuarioId = obtenerUsuarioId();

        if (favoritoRepository.existsByUsuarioIdAndProductoId(
                usuarioId,
                productoId
        )) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "El producto ya esta en favoritos"
            );
        }

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Usuario no encontrado"
                ));

        Producto producto = productoRepository.findById(productoId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Producto no encontrado"
                ));

        Favorito favorito = Favorito.builder()
                .usuario(usuario)
                .producto(producto)
                .fechaAgregado(LocalDateTime.now())
                .build();

        favorito = favoritoRepository.save(favorito);

        return FavoritoDTO.fromEntity(favorito);
    }

    @Transactional
    public void eliminar(Long productoId) {

        Long usuarioId = obtenerUsuarioId();

        if (!favoritoRepository.existsByUsuarioIdAndProductoId(
                usuarioId,
                productoId
        )) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "El producto no esta en favoritos"
            );
        }

        favoritoRepository.deleteByUsuarioIdAndProductoId(
                usuarioId,
                productoId
        );
    }

    @Transactional(readOnly = true)
    public PaginaDTO<FavoritoDTO> listar(
            int pagina,
            int tamanio
    ) {

        Long usuarioId = obtenerUsuarioId();

        Pageable pageable = PageRequest.of(
                pagina,
                tamanio,
                Sort.by(
                        Sort.Direction.DESC,
                        "fechaAgregado"
                )
        );

        Page<Favorito> favoritos =
                favoritoRepository.findByUsuarioId(
                        usuarioId,
                        pageable
                );

        return PaginaDTO.<FavoritoDTO>builder()
                .contenido(
                        favoritos.getContent()
                                .stream()
                                .map(FavoritoDTO::fromEntity)
                                .toList()
                )
                .pagina(favoritos.getNumber())
                .tamanio(favoritos.getSize())
                .totalElementos(favoritos.getTotalElements())
                .totalPaginas(favoritos.getTotalPages())
                .ultima(favoritos.isLast())
                .build();
    }

    @Transactional(readOnly = true)
    public boolean existe(Long productoId) {

        Long usuarioId = obtenerUsuarioId();

        return favoritoRepository.existsByUsuarioIdAndProductoId(
                usuarioId,
                productoId
        );
    }

    private Long obtenerUsuarioId() {

        Object principal = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        return (Long) principal;
    }
    
    @Transactional(readOnly = true)
    public Set<Long> obtenerProductoIdsFavoritos() {

        Long usuarioId = obtenerUsuarioId();

        return favoritoRepository.findProductoIdsByUsuarioId(usuarioId);
    }
}