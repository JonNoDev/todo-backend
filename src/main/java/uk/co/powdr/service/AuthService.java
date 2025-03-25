package uk.co.powdr.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uk.co.powdr.dto.AuthRequest;
import uk.co.powdr.dto.AuthResponse;
import uk.co.powdr.exception.IncorrectPasswordException;
import uk.co.powdr.exception.ResourceNotFoundException;
import uk.co.powdr.model.User;
import uk.co.powdr.repository.UserRepository;
import uk.co.powdr.security.JwtService;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthResponse authenticate(AuthRequest request) {
        log.info("Attempting to authenticate user: {}", request.getEmail());
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException(request.getEmail()));
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IncorrectPasswordException("Your password is incorrect.");
        }

        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("authorities", Set.of(user.getRole()));
        log.info("Successfully authenticated: {}", user.getEmail());
        return AuthResponse.builder()
                .token(jwtService.generateToken(extraClaims, user.getId()))
                .build();
    }
}
