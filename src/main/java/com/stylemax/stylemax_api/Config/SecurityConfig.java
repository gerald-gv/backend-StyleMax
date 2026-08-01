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
                // Todo lo demas (carrito, pedidos, usuarios) queda protegido con JWT mas adelante
                .anyRequest().authenticated()
            );

        return http.build();
    }
}
