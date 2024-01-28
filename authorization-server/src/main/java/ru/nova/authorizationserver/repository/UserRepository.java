package ru.nova.authorizationserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.nova.authorizationserver.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query("""
        SELECT u FROM User u WHERE u.email = :email
    """)
    Optional<User> findByEmail(String email);
}
