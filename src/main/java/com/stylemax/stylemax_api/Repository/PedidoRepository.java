package com.stylemax.stylemax_api.Repository;

import com.stylemax.stylemax_api.DTO.PedidoClienteResumenProjection;
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
    
    
    // CLIENTE
    
    @Query("""
    	    SELECT
    	        p.id AS id,
    	        p.fechaPedido AS fechaPedido,
    	        p.estado AS estado,
    	        p.total AS total,

    	        COALESCE(SUM(d.cantidad), 0) AS cantidadProductos,

    	        (
    	            SELECT prod.nombre
    	            FROM DetallePedido d2
    	            JOIN d2.producto prod
    	            WHERE d2.id = (
    	                SELECT MIN(d3.id)
    	                FROM DetallePedido d3
    	                WHERE d3.pedido = p
    	            )
    	        ) AS primerProducto,

    	        (
    	            SELECT prod.imagen
    	            FROM DetallePedido d2
    	            JOIN d2.producto prod
    	            WHERE d2.id = (
    	                SELECT MIN(d3.id)
    	                FROM DetallePedido d3
    	                WHERE d3.pedido = p
    	            )
    	        ) AS primeraImagen

    	    FROM Pedido p
    	    LEFT JOIN p.detalles d
    	    WHERE p.usuario.id = :usuarioId
    	    AND (
		        :estados IS NULL
		        OR p.estado IN :estados
		    )

    	    GROUP BY
    	        p.id,
    	        p.fechaPedido,
    	        p.estado,
    	        p.total

    	    ORDER BY p.fechaPedido DESC
    	""")
    	Page<PedidoClienteResumenProjection> buscarResumenCliente(
    	    @Param("usuarioId") Long usuarioId,
    	    @Param("estados") List<PedidoEstado> estados,
    	    Pageable pageable
    	);


        @Query("""
            SELECT DISTINCT p
            FROM Pedido p
            LEFT JOIN FETCH p.detalles d
            LEFT JOIN FETCH d.producto
            WHERE p.id = :pedidoId
            AND p.usuario.id = :usuarioId
            """)
        Optional<Pedido> buscarDetalleCliente(
                @Param("pedidoId") Long pedidoId,
                @Param("usuarioId") Long usuarioId
        );

    
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
