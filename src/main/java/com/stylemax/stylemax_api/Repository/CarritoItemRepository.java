package com.stylemax.stylemax_api.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.stylemax.stylemax_api.Entity.CarritoItem;

@Repository
public interface CarritoItemRepository extends JpaRepository<CarritoItem, Long>{
	
	Optional<CarritoItem> findByCarritoIdAndProductoId(Long carritoid, Long productoId);
	
}
