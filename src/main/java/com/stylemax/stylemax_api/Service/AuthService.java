package com.stylemax.stylemax_api.Service;

import com.stylemax.stylemax_api.DTO.LoginResponseDTO;
import com.stylemax.stylemax_api.Entity.Usuario;
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
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    public LoginResponseDTO login(String correo, String password) {
        Usuario usuario = usuarioRepository.findByCorreoAndActivoTrue(correo)
                .orElseThrow(this::credencialesInvalidas);

        if (!passwordEncoder.matches(password, usuario.getPassword())) {
            throw credencialesInvalidas();
        }

        String token = jwtProvider.generarToken(usuario);

        return new LoginResponseDTO(
                token,
                usuario.getId(),
                usuario.getNombre(),
                usuario.getCorreo(),
                usuario.getRol().getNombre());
    }
    private ResponseStatusException credencialesInvalidas() {
        return new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Correo o contrasena invalidos");
    }
}

