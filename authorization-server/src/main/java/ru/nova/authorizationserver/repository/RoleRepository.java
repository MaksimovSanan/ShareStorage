package ru.nova.authorizationserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.nova.authorizationserver.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
