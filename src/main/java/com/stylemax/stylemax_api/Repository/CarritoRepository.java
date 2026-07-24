package com.stylemax.stylemax_api.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.stylemax.stylemax_api.Entity.Carrito;


@Repository
public interface CarritoRepository extends JpaRepository<Carrito, Long>{
	
	Optional<Carrito> findByUsuarioId(Long usuarioId);
	
}
