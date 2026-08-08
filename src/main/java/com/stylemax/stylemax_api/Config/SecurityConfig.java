package com.stylemax.stylemax_api.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfigurationSource;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final CorsConfigurationSource corsConfigurationSource;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource))
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                // Catalogo publico: cualquiera puede consultar productos, categorias y marcas
                .requestMatchers(HttpMethod.GET,
                        "/api/productos/**",
                        "/api/categorias/**",
                        "/api/marcas/**")
                    .permitAll()
                    // TODO(seguridad) TEMPORAL: sin JWT no hay forma de autenticar
                            // al usuario, asi que este endpoint queda abierto solo para
                            // probar el flujo de carrito con Postman. Mientras esto siga
                            // asi, usuarioId viaja SIN VALIDAR en cada request -- cualquiera
                            // puede leer u operar el CARRITO, PAGOS o los PEDIDOS de otro usuario con solo
                            // cambiar el numero. Sacar este permitAll en cuanto exista JWT.
                            .requestMatchers("/api/carrito/**", "/api/pedidos/**", "/api/pagos/**")
                            .permitAll()
                    // Lo demas (pedidos, usuarios) queda protegido con JWT mas adelante
                    .anyRequest().authenticated()
            );

        return http.build();
    }
}
