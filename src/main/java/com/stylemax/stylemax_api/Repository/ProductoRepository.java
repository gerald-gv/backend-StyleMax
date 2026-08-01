package com.stylemax.stylemax_api.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
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
	List<Producto> buscarCatalogo(@Param("categoriaId") Long categoriaId,
			@Param("marcaId") Long marcaId,
			@Param("q") String q);
}
