package com.stylemax.stylemax_api.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.stylemax.stylemax_api.Entity.Direccion;

public interface DireccionRepository extends JpaRepository<Direccion, Long> {

    Optional<Direccion> findByUsuarioId(Long usuarioId);

}