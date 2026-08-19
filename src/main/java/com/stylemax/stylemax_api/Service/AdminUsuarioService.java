package com.stylemax.stylemax_api.Service;

import java.util.Locale;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.stylemax.stylemax_api.DTO.PaginaDTO;
import com.stylemax.stylemax_api.DTO.admin.ActualizarUsuarioAdminRequest;
import com.stylemax.stylemax_api.DTO.admin.RestablecerPasswordAdminRequest;
import com.stylemax.stylemax_api.DTO.admin.UsuarioAdminDTO;
import com.stylemax.stylemax_api.DTO.admin.UsuarioEstadisticasDTO;
import com.stylemax.stylemax_api.Entity.Direccion;
import com.stylemax.stylemax_api.Entity.Usuario;
import com.stylemax.stylemax_api.Repository.DireccionRepository;
import com.stylemax.stylemax_api.Repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminUsuarioService {

    private static final String ROL_ADMINISTRADOR = "ADMINISTRADOR";
    private static final String ROL_CLIENTE = "CLIENTE";

    private final UsuarioRepository usuarioRepository;
    private final DireccionRepository direccionRepository;
    private final PasswordEncoder passwordEncoder;
    
    private static final int TAMANIO_PAGINA_ADMIN = 20;

    @Transactional(readOnly = true)
    public PaginaDTO<UsuarioAdminDTO> listarUsuarios( int pagina, String rol, String q) {

        if (pagina < 0) {
            pagina = 0;
        }

        Pageable pageable = PageRequest.of( pagina, TAMANIO_PAGINA_ADMIN, Sort.by( Sort.Direction.DESC,"id"));

        String rolFiltro = rol != null && !rol.isBlank() ? rol.trim().toUpperCase(Locale.ROOT) : null;

        String busqueda = q != null && !q.isBlank() ? q.trim() : null;

        Page<Usuario> usuarios;

        if (busqueda != null) {

            usuarios = usuarioRepository.buscar(
                    busqueda,
                    rolFiltro,
                    pageable
            );

        } else if (rolFiltro != null) {
            usuarios = usuarioRepository.findByRolNombre(rolFiltro, pageable);
        } else {
            usuarios = usuarioRepository.findAll(pageable);
        }

        return construirPagina(usuarios);
    }

    @Transactional(readOnly = true)
    public UsuarioAdminDTO obtenerUsuario(Long id) {

        Usuario usuario = obtenerPorId(id);

        Direccion direccion = direccionRepository.findByUsuarioId(id).orElse(null);

        return UsuarioAdminDTO.fromEntity(usuario,direccion);
    }

    @Transactional
    public UsuarioAdminDTO actualizarUsuario(Long id,ActualizarUsuarioAdminRequest request) {

        Usuario usuario = obtenerPorId(id);

        String correoNuevo = request.correo().trim();

        if (usuarioRepository.existsByCorreoAndIdNot(correoNuevo,id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,"Ese correo ya está registrado");
        }

        usuario.setNombre(request.nombre().trim());

        usuario.setApellido(request.apellido().trim());

        usuario.setCorreo(correoNuevo);

        usuario.setTelefono(request.telefono() != null ? request.telefono().trim() : null);

        usuario = usuarioRepository.save(usuario);

        Direccion direccion = direccionRepository.findByUsuarioId(id).orElse(null);

        return UsuarioAdminDTO.fromEntity(usuario,direccion);
    }

    @Transactional
    public void restablecerPassword(Long id,RestablecerPasswordAdminRequest request) {

        Usuario usuario = obtenerPorId(id);

        if (ROL_ADMINISTRADOR.equalsIgnoreCase(usuario.getRol().getNombre())) {

            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No puedes cambiar la contraseña de otro administrador");
        }

        usuario.setPassword(passwordEncoder.encode(request.nuevaPassword()));

        usuarioRepository.save(usuario);
    }
    
    @Transactional
    public UsuarioAdminDTO activarUsuario(Long id) {

        Usuario usuario = obtenerPorId(id);

        if (!ROL_CLIENTE.equalsIgnoreCase(usuario.getRol().getNombre())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,"Solo se pueden reactivar usuarios clientes");
        }

        usuario.setActivo(true);

        usuario = usuarioRepository.save(usuario);

        Direccion direccion = direccionRepository.findByUsuarioId(id).orElse(null);

        return UsuarioAdminDTO.fromEntity(usuario,direccion);

    }

    @Transactional
    public void eliminarUsuario(Long id) {

        Usuario usuario = obtenerPorId(id);

        if (ROL_ADMINISTRADOR.equalsIgnoreCase(usuario.getRol().getNombre())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,"No puedes desactivar a otro administrador");
        }

        usuario.setActivo(false);

        usuarioRepository.save(usuario);
    }

    @Transactional(readOnly = true)
    public UsuarioEstadisticasDTO obtenerEstadisticas() {

        long totalUsuarios = usuarioRepository.count();

        long administradores = usuarioRepository.countByRolNombre(ROL_ADMINISTRADOR);

        long clientes = usuarioRepository.countByRolNombre(ROL_CLIENTE);

        long usuariosActivos = usuarioRepository.countByActivoTrue();

        long usuariosInactivos = totalUsuarios - usuariosActivos;

        return UsuarioEstadisticasDTO.builder()
                .totalUsuarios(totalUsuarios)
                .administradores(administradores)
                .clientes(clientes)
                .usuariosActivos(usuariosActivos)
                .usuariosInactivos(usuariosInactivos)
                .build();
    }

    private Usuario obtenerPorId(Long id) {

        return usuarioRepository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND,"Usuario no encontrado")
                );
    }

    private PaginaDTO<UsuarioAdminDTO> construirPagina(
            Page<Usuario> pagina
    ) {

        return PaginaDTO.<UsuarioAdminDTO>builder()

                .contenido(pagina.getContent()
                                .stream()
                                .map(usuario ->
                                        UsuarioAdminDTO.fromEntity(
                                                usuario,
                                                null
                                        )
                                )
                                .toList()
                )

                .pagina(pagina.getNumber())
                .tamanio(pagina.getSize())
                .totalElementos(pagina.getTotalElements())
                .totalPaginas(pagina.getTotalPages())
                .ultima(pagina.isLast())

                .build();
    }
}