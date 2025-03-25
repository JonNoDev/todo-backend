package uk.co.powdr.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uk.co.powdr.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
}
