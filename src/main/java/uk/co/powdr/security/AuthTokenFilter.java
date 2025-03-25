package uk.co.powdr.security;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import uk.co.powdr.exception.ResourceNotFoundException;
import uk.co.powdr.model.User;
import uk.co.powdr.repository.UserRepository;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuthTokenFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userId;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        jwt = authHeader.substring(7);
        try {
            if (jwtService.validateToken(jwt)) {
                userId = jwtService.extractUserId(jwt);
                if (userId != null) {
                    User user = userRepository.findById(Long.valueOf(userId))
                            .orElseThrow(() -> new ResourceNotFoundException("User with id " + userId + " not found"));
                    log.info("User found for authentication: {}", user.getEmail());
                    Claims claims = jwtService.extractAllClaims(jwt);
                    UserDetails userDetails = jwtService.extractUserDetails(jwt);
                    JwtAuthentication authentication = new JwtAuthentication(userDetails, jwt, claims);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        } catch (Exception e) {
            log.error("Cannot set user authentication: {}", e.getMessage());
        }
        filterChain.doFilter(request, response);
    }
}
