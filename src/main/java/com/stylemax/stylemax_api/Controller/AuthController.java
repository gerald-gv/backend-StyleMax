package com.stylemax.stylemax_api.Controller;

import com.stylemax.stylemax_api.DTO.LoginRequest;
import com.stylemax.stylemax_api.DTO.LoginResponseDTO;
import com.stylemax.stylemax_api.DTO.RegisterRequest;
import com.stylemax.stylemax_api.Service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public LoginResponseDTO login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request.correo(), request.password());
    }

    @PostMapping("/register")
    public LoginResponseDTO register(@Valid @RequestBody RegisterRequest request) {
        return authService.register(request);
    }
}
