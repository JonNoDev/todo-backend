package uk.co.powdr.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uk.co.powdr.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
