package com.stylemax.stylemax_api.Service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.stylemax.stylemax_api.DTO.ActualizarPerfilRequest;
import com.stylemax.stylemax_api.DTO.PerfilDTO;
import com.stylemax.stylemax_api.Entity.Usuario;
import com.stylemax.stylemax_api.Repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PerfilService {

    private final UsuarioRepository usuarioRepository;

    @Transactional(readOnly = true)
    public PerfilDTO obtenerPerfil(Long usuarioId) {

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResponseStatusException( HttpStatus.NOT_FOUND, "Usuario no encontrado"));

        return PerfilDTO.fromEntity(usuario);
    }

    @Transactional
    public PerfilDTO actualizarPerfil(Long usuarioId, ActualizarPerfilRequest request) {

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResponseStatusException( HttpStatus.NOT_FOUND,  "Usuario no encontrado"));

        usuario.setNombre(request.nombre());
        usuario.setApellido(request.apellido());
        usuario.setTelefono(request.telefono());

        usuario = usuarioRepository.save(usuario);

        return PerfilDTO.fromEntity(usuario);
    }
}