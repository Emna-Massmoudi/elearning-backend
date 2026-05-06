package com.elearning.elearning_api.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.*;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session ->
                    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth

                // OPTIONS pour CORS
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                // Auth public
                .requestMatchers("/api/auth/**").permitAll()

                // Swagger public
                .requestMatchers(
                    "/swagger-ui.html",
                    "/swagger-ui/**",
                    "/v3/api-docs/**",
                    "/swagger-resources/**",
                    "/webjars/**"
                ).permitAll()

                // Uploads public
                .requestMatchers("/uploads/**").permitAll()

                // Routes publiques GET
                .requestMatchers(HttpMethod.GET, "/api/cours").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/cours/**").permitAll()

                .requestMatchers(HttpMethod.GET, "/api/categories").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/categories/**").permitAll()

                .requestMatchers(HttpMethod.GET, "/api/sous-categories").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/sous-categories/**").permitAll()

                // Gestion catégories par ADMIN
                .requestMatchers(HttpMethod.POST, "/api/categories/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/categories/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/categories/**").hasRole("ADMIN")

                // Gestion sous-catégories par ADMIN
                .requestMatchers(HttpMethod.POST, "/api/sous-categories/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/sous-categories/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/sous-categories/**").hasRole("ADMIN")

                // Admin
                .requestMatchers("/api/admin/**").hasRole("ADMIN")

                // Étudiant
                .requestMatchers("/api/etudiant/**").hasRole("ETUDIANT")

                // Formateur
                .requestMatchers("/api/formateurs/en-attente").hasRole("ADMIN")
                .requestMatchers("/api/formateurs/*/accepter").hasRole("ADMIN")
                .requestMatchers("/api/formateurs/*/refuser").hasRole("ADMIN")
                .requestMatchers("/api/formateurs/*/candidature").hasRole("FORMATEUR")
                .requestMatchers("/api/formateurs/*").authenticated()

                // Le reste nécessite connexion
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOriginPatterns(List.of(
            "http://localhost:4200",
            "https://*.web.app",
            "https://*.firebaseapp.com"
        ));

        configuration.setAllowedMethods(List.of(
            "GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"
        ));

        configuration.setAllowedHeaders(List.of("*"));
        configuration.setExposedHeaders(List.of("Authorization"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}