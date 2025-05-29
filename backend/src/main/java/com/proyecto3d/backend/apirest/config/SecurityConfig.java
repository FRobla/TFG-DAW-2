package com.proyecto3d.backend.apirest.config;
 
import java.util.Arrays;
 
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
 
/**
 * Configuración de seguridad para la aplicación
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {
 
    /**
     * Configura los filtros de seguridad HTTP
     * @param http Configuración de seguridad HTTP
     * @return Cadena de filtros configurada
     * @throws Exception Si ocurre algún error
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(authz -> authz
                // Permitir acceso público a endpoints específicos para la integración con el frontend
                .requestMatchers("/api/**").permitAll()
                .requestMatchers("/api/categorias/**").permitAll()
                .requestMatchers("/api/search/**").permitAll()
                .anyRequest().permitAll()
            );

        return http.build();
    }
 
    /**
     * Configura las opciones CORS para permitir la comunicación con el frontend
     * @return Configuración CORS
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList(
            "http://localhost:4200",
            "file://",
            "null" // Para archivos abiertos directamente desde el navegador
        ));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    } 
}