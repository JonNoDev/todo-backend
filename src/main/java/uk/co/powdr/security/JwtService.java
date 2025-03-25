package uk.co.powdr.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtService {

    @Value("${app.secret}")
    private static String SECRET_KEY;

    public String generateToken(Map<String, Object> extraClaims, String userId) {
        return Jwts.builder()
                .claims(extraClaims)
                .subject(userId)
                .signWith(getSigningKey(), Jwts.SIG.HS512)
                .issuedAt(Date.from(Instant.now()))
                .expiration(Date.from(Instant.now().plusSeconds(86400)))
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            return !extractClaim(token, Claims::getExpiration).before(Date.from(Instant.now()));
        } catch (ExpiredJwtException e) {
            return false;
        }
    }

    public UserDetails extractUserDetails(String token) {
        String username = extractUserId(token);
        if (username == null) {
            throw new BadCredentialsException("Invalid token");
        }

        return new User(username, "", extractAuthorities(token));
    }

    public String extractUserId(String token) {
        return extractClaim(token, Claims::getSubject);
    }


    public List<GrantedAuthority> extractAuthorities(String token) {
        Claims claims = extractAllClaims(token);
        List<String> role = (List<String>) claims.get("authorities");
        return List.of(new SimpleGrantedAuthority(role.get(0)));
    }

    public Claims extractAllClaims(String token) {
        return Jwts
                .parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
