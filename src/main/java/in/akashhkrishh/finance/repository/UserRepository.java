package in.akashhkrishh.finance.repository;

import in.akashhkrishh.finance.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    List<User> findByEmail(String email);
    boolean existsByEmail(String email);
}
