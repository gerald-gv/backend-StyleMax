package com.stylemax.stylemax_api.Repository;

import java.util.List;
import java.util.Optional;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.stylemax.stylemax_api.Entity.Producto;

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
			AND (:q IS NULL OR LOWER(p.nombre) LIKE LOWER(CONCAT('%', :q, '%')))
			ORDER BY p.nombre ASC
			""")
	List<Producto> buscarCatalogo(@Param("categoriaId") Long categoriaId, @Param("marcaId") Long marcaId, @Param("q") String q);

	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("SELECT p FROM Producto p WHERE p.id =:id")
	Optional<Producto> buscarConLockParaActualizarStock(@Param("id") Long id);
	
	// ADMIN
	
	@Query("""
            SELECT p
            FROM Producto p
            JOIN FETCH p.marca
            JOIN FETCH p.categoria
            ORDER BY p.id DESC
            """)
    List<Producto> findAllParaAdmin();
	
	
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
