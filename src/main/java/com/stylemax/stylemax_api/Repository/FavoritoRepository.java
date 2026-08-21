package com.stylemax.stylemax_api.Repository;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.stylemax.stylemax_api.Entity.Favorito;

public interface FavoritoRepository extends JpaRepository<Favorito, Long> {

    Page<Favorito> findByUsuarioId(
            Long usuarioId,
            Pageable pageable
    );

    Optional<Favorito> findByUsuarioIdAndProductoId(
            Long usuarioId,
            Long productoId
    );

    boolean existsByUsuarioIdAndProductoId(
            Long usuarioId,
            Long productoId
    );

    void deleteByUsuarioIdAndProductoId(
            Long usuarioId,
            Long productoId
    );

    @Query("""
            SELECT f.producto.id
            FROM Favorito f
            WHERE f.usuario.id = :usuarioId
            """)
    Set<Long> findProductoIdsByUsuarioId(
            @Param("usuarioId") Long usuarioId
    );

}