package uk.co.powdr.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import uk.co.powdr.exception.AccessLevelException;

import java.util.Arrays;
import java.util.Collection;

@Service
@Slf4j
public class RoleValidator {

    public static String ROLE_ADMIN = "ROLE_ADMIN";
    public static String ROLE_USER = "ROLE_USER";

    public void validateRole(String requiredRole) {
        log.info("Validating a role!");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        auth.getDetails();
        if (!hasRole(requiredRole, auth.getAuthorities())) {
            throw new AccessLevelException("You do not have permission to perform this action.");
        }
    }

    public void validateRole(String... requiredRoles) {
        log.info("Validating a list of roles!");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (Arrays.stream(requiredRoles).noneMatch(role -> hasRole(role, auth.getAuthorities()))) {
            throw new AccessLevelException("You do not have the required permission to perform this action.");
        }
    }

    public void validateAdminOrUser(String userId) {
        log.info("Validating if user is an admin or username matches JWT! {} {}", userId, getSubject());
        Collection<? extends GrantedAuthority> authorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        if (!hasRole(ROLE_ADMIN, authorities)
                && !getSubject().equals(userId)) {
            throw new AccessLevelException("You do not have permission to perform this action");
        }
    }

    public String getSubject() { return SecurityContextHolder.getContext().getAuthentication().getName(); }

    private boolean hasRole(String role, Collection<? extends GrantedAuthority> authorities) {
        return authorities.stream().anyMatch(a -> a.getAuthority().equals(role));
    }
}
