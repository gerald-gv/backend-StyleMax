package com.stylemax.stylemax_api.Repository;

import java.util.List;
import java.util.Optional;

import jakarta.persistence.LockModeType;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.stylemax.stylemax_api.DTO.admin.ProductosEstadisticasProjection;
import com.stylemax.stylemax_api.Entity.Producto;
import com.stylemax.stylemax_api.Enums.Fit;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
	
	Optional<Producto> findBySlug(String slug);
	
	List<Producto> findByActivoTrue();
	
	List<Producto> findByDestacadoTrue();
	
	List<Producto> findTop4ByDestacadoTrueAndActivoTrue();
	
	List<Producto> findByCategoriaId(Long categoriaId);
	
	List<Producto> findByMarcaId(Long marcaId);
	
	List<Producto> findByNombreContainingIgnoreCase(String nombre);

	
	@Query("""
			SELECT p FROM Producto p
			WHERE p.activo = true
			AND (:categoriaId IS NULL OR p.categoria.id = :categoriaId)
			AND (:marcaId IS NULL OR p.marca.id = :marcaId)
			AND (:fit IS NULL OR p.fit = :fit)
			AND (:q IS NULL OR LOWER(p.nombre) LIKE LOWER(CONCAT('%', :q, '%')))
			ORDER BY p.nombre ASC
			""")
	Page<Producto> buscarCatalogo(@Param("categoriaId") Long categoriaId, @Param("marcaId") Long marcaId, @Param("fit") Fit  fit, @Param("q") String q, Pageable pageable);

	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("SELECT p FROM Producto p WHERE p.id =:id")
	Optional<Producto> buscarConLockParaActualizarStock(@Param("id") Long id);
	
	// ADMIN
	
	@Query("""
	        SELECT p
	        FROM Producto p
	        JOIN FETCH p.marca
	        JOIN FETCH p.categoria
	        WHERE (
	            :q IS NULL
	            OR LOWER(p.nombre) LIKE LOWER(CONCAT('%', :q, '%'))
	            OR LOWER(p.slug) LIKE LOWER(CONCAT('%', :q, '%'))
	            OR LOWER(p.marca.nombre) LIKE LOWER(CONCAT('%', :q, '%'))
	            OR LOWER(p.categoria.nombre) LIKE LOWER(CONCAT('%', :q, '%'))
	        )
	        ORDER BY p.id DESC
	        """)
	Page<Producto> buscarParaAdmin( @Param("q") String q, Pageable pageable);
	
	
	@Query("""
	        SELECT
	            COUNT(p) AS total,
	            SUM(CASE WHEN p.activo = true THEN 1 ELSE 0 END) AS activos,
	            SUM(CASE WHEN p.stock = 0 THEN 1 ELSE 0 END) AS sinStock,
	            SUM(CASE WHEN p.destacado = true THEN 1 ELSE 0 END) AS destacados
	        FROM Producto p
	        """)
	ProductosEstadisticasProjection obtenerEstadisticasAdmin();
	
	@Query("""
            SELECT p 
            FROM Producto p 
            JOIN FETCH p.marca 
            JOIN FETCH p.categoria 
            WHERE p.id = :id
            """)
    Optional<Producto> findByIdParaAdmin(@Param("id") Long id);
	
	boolean existsBySlug(String slug);

}
