package com.stylemax.stylemax_api.Service;

import com.stylemax.stylemax_api.DTO.LoginResponseDTO;
import com.stylemax.stylemax_api.DTO.RegisterRequest;
import com.stylemax.stylemax_api.Entity.Rol;
import com.stylemax.stylemax_api.Entity.Usuario;
import com.stylemax.stylemax_api.Repository.RolRepository;
import com.stylemax.stylemax_api.Repository.UsuarioRepository;
import com.stylemax.stylemax_api.Security.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AuthService {

    private static final String ROL_POR_DEFECTO = "CLIENTE";

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    public LoginResponseDTO login(String correo, String password) {
        Usuario usuario = usuarioRepository.findByCorreoAndActivoTrue(correo)
                .orElseThrow(this::credencialesInvalidas);

        if (!passwordEncoder.matches(password, usuario.getPassword())) {
            throw credencialesInvalidas();
        }

        return construirRespuesta(usuario);
    }

    public LoginResponseDTO register(RegisterRequest request) {
        if (usuarioRepository.existsByCorreo(request.correo())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ese correo ya esta registrado");
        }

        Rol rolCliente = rolRepository.findByNombre(ROL_POR_DEFECTO)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                        "No existe el rol por defecto " + ROL_POR_DEFECTO));

        Usuario usuario = Usuario.builder()
                .nombre(request.nombre())
                .apellido(request.apellido())
                .correo(request.correo())
                .password(passwordEncoder.encode(request.password()))
                .telefono(request.telefono())
                .activo(true)
                .rol(rolCliente)
                .build();

        usuario = usuarioRepository.save(usuario);

        return construirRespuesta(usuario);
    }

    private LoginResponseDTO construirRespuesta(Usuario usuario) {
        String token = jwtProvider.generarToken(usuario);
        return new LoginResponseDTO(
                token,
                usuario.getId(),
                usuario.getNombre(),
                usuario.getCorreo(),
                usuario.getRol().getNombre()
        );
    }

    private ResponseStatusException credencialesInvalidas() {
        return new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Correo o contrasena invalidos");
    }
}

