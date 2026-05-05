package com.elearning.elearning_api.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;

import java.io.IOException;
import java.util.List;
@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;

    // Centralisé ici pour cohérence avec SecurityConfig
    private static final List<String> PUBLIC_PATHS = List.of(
        "/api/auth/",
        "/swagger-ui/",
        "/v3/api-docs",
        "/swagger-resources/",
        "/webjars/",
        "/uploads/",
        "/ping"
    );

    private static final List<String> PUBLIC_GET_PATHS = List.of(
        "/api/categories",
        "/api/cours"
    );

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        // ✅ getRequestURI() est plus fiable que getServletPath()
        String path = request.getRequestURI();
        String method = request.getMethod();

        if (method.equalsIgnoreCase("OPTIONS")) return true;
        if (path.startsWith("/api/auth/")) return true;
        if (path.startsWith("/swagger-ui/")) return true;
        if (path.startsWith("/v3/api-docs")) return true;
        if (path.startsWith("/uploads/")) return true;
        if (path.equals("/ping")) return true;

        if (method.equalsIgnoreCase("GET")) {
            return path.startsWith("/api/categories")
                || path.startsWith("/api/cours");
        }

        return false;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);

        try {
            if (jwtUtil.isTokenValid(token)) {
                String email = jwtUtil.extractEmail(token);

                if (email != null &&
                    SecurityContextHolder.getContext().getAuthentication() == null) {

                    UserDetails userDetails =
                        userDetailsService.loadUserByUsername(email);

                    UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());

                    authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        } catch (ExpiredJwtException e) {
            sendJsonError(response, HttpServletResponse.SC_UNAUTHORIZED, "Token expiré");
            return;
        } catch (JwtException e) {
            sendJsonError(response, HttpServletResponse.SC_UNAUTHORIZED, "Token invalide");
            return;
        }
        // Les autres exceptions (DB down, etc.) → on laisse passer sans auth

        filterChain.doFilter(request, response);
    }

    private void sendJsonError(HttpServletResponse response, int status, String message)
            throws IOException {
        response.setStatus(status);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write("{\"error\":\"" + message + "\"}");
    }
}