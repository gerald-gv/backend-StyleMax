package com.stylemax.stylemax_api.Service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.stylemax.stylemax_api.DTO.ActualizarDireccionRequest;
import com.stylemax.stylemax_api.DTO.DireccionDTO;
import com.stylemax.stylemax_api.Entity.Direccion;
import com.stylemax.stylemax_api.Entity.Usuario;
import com.stylemax.stylemax_api.Repository.DireccionRepository;
import com.stylemax.stylemax_api.Repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DireccionService {

    private final DireccionRepository direccionRepository;
    private final UsuarioRepository usuarioRepository;


    @Transactional(readOnly = true)
    public DireccionDTO obtenerDireccion(Long usuarioId) {

        Direccion direccion = direccionRepository.findByUsuarioId(usuarioId)
        		.orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND,"El usuario no tiene una dirección registrada")
                );

        return DireccionDTO.fromEntity(direccion);
    }


    @Transactional
    public DireccionDTO guardarDireccion( Long usuarioId,ActualizarDireccionRequest request) {

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND,"Usuario no encontrado")
                );


        Direccion direccion = direccionRepository
                .findByUsuarioId(usuarioId)
                .orElseGet(() ->
                        Direccion.builder()
                                .usuario(usuario)
                                .build()
                );


        direccion.setDepartamento(request.departamento());
        direccion.setProvincia(request.provincia());
        direccion.setDistrito(request.distrito());
        direccion.setDireccionCompleta(request.direccionCompleta());
        direccion.setReferencia(request.referencia());


        direccion = direccionRepository.save(direccion);


        return DireccionDTO.fromEntity(direccion);
    }
}
