package ru.maksimov.authorizationserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.maksimov.authorizationserver.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
