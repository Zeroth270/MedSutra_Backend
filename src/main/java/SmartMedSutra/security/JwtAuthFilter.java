package SmartMedSutra.security;

import SmartMedSutra.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");

        // Skip if no Bearer token
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            final String token = authHeader.substring(7);
            final String email = jwtService.extractEmail(token);

            // Authenticate if not already authenticated
            if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                userRepository.findByEmail(email).ifPresent(user -> {
                    if (jwtService.isTokenValid(token, email)) {

                        var authorities = List.of(
                                new SimpleGrantedAuthority("ROLE_" + user.getRole().name())
                        );

                        var authToken = new UsernamePasswordAuthenticationToken(
                                user, null, authorities
                        );
                        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    }
                });
            }
        } catch (Exception e) {
            log.error("JWT Authentication failed: {}", e.getMessage());
        }

        filterChain.doFilter(request, response);
    }
}

