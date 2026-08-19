package com.stylemax.stylemax_api.Repository;

import com.stylemax.stylemax_api.Entity.Pedido;
import com.stylemax.stylemax_api.Enums.PedidoEstado;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PedidoRepository  extends JpaRepository<Pedido, Long> {
    List<Pedido> findByUsuarioId(Long usuarioId);
    List<Pedido> findByEstadoAndFechaPedidoBefore(PedidoEstado estado, LocalDateTime fecha);
    
    // ADMIN
    Page<Pedido> findByEstado(PedidoEstado estado,Pageable pageable);

    @Query("""
        SELECT p
        FROM Pedido p
        JOIN p.usuario u
        WHERE
            (
                LOWER(u.nombre) LIKE LOWER(CONCAT('%', :q, '%'))
                OR LOWER(u.apellido) LIKE LOWER(CONCAT('%', :q, '%'))
                OR LOWER(u.correo) LIKE LOWER(CONCAT('%', :q, '%'))
            )
            AND (
                :estado IS NULL
                OR p.estado = :estado
            )
        """)
    Page<Pedido> buscar(@Param("q") String q,@Param("estado") PedidoEstado estado,Pageable pageable);
    
    @Query("""
    	    SELECT DISTINCT p
    	    FROM Pedido p
    	    JOIN FETCH p.usuario u
    	    LEFT JOIN FETCH p.detalles d
    	    LEFT JOIN FETCH d.producto
    	    WHERE p.id = :id
    	    """)
    	Optional<Pedido> buscarDetalleAdmin(@Param("id") Long id);

    long countByEstado (PedidoEstado estado);
}
