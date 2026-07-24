package com.stylemax.stylemax_api.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.stylemax.stylemax_api.Entity.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long>{
	
	Optional<Usuario> findByCorreoAndActivoTrue(String correo);
	
	boolean existsByCorreo(String correo);
	
	List<Usuario> findByActivoTrue();
	
	List<Usuario> findByRolNombre(String nombre);
}
