package com.stylemax.stylemax_api.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.stylemax.stylemax_api.DTO.admin.MarcaEstadisticasProjection;
import com.stylemax.stylemax_api.Entity.Marca;

@Repository
public interface MarcaRepository extends JpaRepository<Marca, Long> {

    List<Marca> findByActivoTrue();
    
    List<Marca> findAllByOrderByNombreAsc();
    
    List<Marca> findByActivoTrueOrderByNombreAsc();
    
    Optional<Marca> findByNombreIgnoreCase(String nombre);

    boolean existsByNombreIgnoreCase(String nombre);
    
    // ADMIN
    
    Page<Marca> findAllByOrderByNombreAsc(Pageable pageable);
    Page<Marca> findByNombreContainingIgnoreCaseOrderByNombreAsc(String nombre,Pageable pageable);
    
    
    @Query("""
            SELECT
                COUNT(m) AS total,
                SUM(CASE WHEN m.activo = true THEN 1 ELSE 0 END) AS activas,
                SUM(CASE WHEN m.activo = false THEN 1 ELSE 0 END) AS inactivas
            FROM Marca m
            """)
    MarcaEstadisticasProjection obtenerEstadisticasAdmin();
}
