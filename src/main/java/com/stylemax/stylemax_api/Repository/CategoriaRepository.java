package com.stylemax.stylemax_api.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.stylemax.stylemax_api.Entity.Categoria;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long>{
	
	List<Categoria> findByActivoTrue();
	
	Optional<Categoria> findByNombreIgnoreCase(String nombre);
	
	boolean existsByNombreIgnoreCase(String nombre);
	
	List<Categoria> findAllByOrderByNombreAsc();
}
