package com.stylemax.stylemax_api.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.stylemax.stylemax_api.DTO.admin.CategoriaEstadisticasProjection;
import com.stylemax.stylemax_api.Entity.Categoria;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long>{
	
	List<Categoria> findByActivoTrue();
	
	Optional<Categoria> findByNombreIgnoreCase(String nombre);
	
	boolean existsByNombreIgnoreCase(String nombre);
	
	List<Categoria> findAllByOrderByNombreAsc();
	
	List<Categoria> findByActivoTrueOrderByNombreAsc();
	
	Page<Categoria> findAllByOrderByNombreAsc(Pageable pageable);

    Page<Categoria> findByNombreContainingIgnoreCaseOrderByNombreAsc(String nombre,Pageable pageable);
    
    @Query("""
            SELECT
            COUNT(c) AS total,
            SUM(
            	CASE
            		WHEN c.activo = true THEN 1
            		ELSE 0
                    END
                ) AS activas,
            SUM(
            	CASE
            		WHEN c.activo = false THEN 1
            		ELSE 0
                    END
                ) AS inactivas
            FROM Categoria c
            """)
    CategoriaEstadisticasProjection obtenerEstadisticasAdmin();
}
