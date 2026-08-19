package com.stylemax.stylemax_api.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.stylemax.stylemax_api.Entity.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long>{
	
	Optional<Usuario> findByCorreoAndActivoTrue(String correo);
	
	boolean existsByCorreo(String correo);
	
	boolean existsByCorreoAndIdNot(String correo, Long id);
	
	List<Usuario> findByActivoTrue();
	
	List<Usuario> findByRolNombre(String nombre);
	
	// ADMIN
    Page<Usuario> findByRolNombre(String nombre, Pageable pageable);
    
    @Query("""
    	    SELECT u
    	    FROM Usuario u
    	    WHERE
    	        (
    	            LOWER(u.nombre) LIKE LOWER(CONCAT('%', :q, '%'))
    	            OR LOWER(u.apellido) LIKE LOWER(CONCAT('%', :q, '%'))
    	            OR LOWER(u.correo) LIKE LOWER(CONCAT('%', :q, '%'))
    	        )
    	        AND (
    	            :rol IS NULL
    	            OR LOWER(u.rol.nombre) = LOWER(:rol)
    	        )
    	""")
    	Page<Usuario> buscar(
    	        @Param("q") String q,
    	        @Param("rol") String rol,
    	        Pageable pageable
    	);
    
    long countByRolNombre(String nombre);

    long countByActivoTrue();
}
