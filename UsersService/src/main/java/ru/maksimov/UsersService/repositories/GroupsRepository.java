package ru.maksimov.UsersService.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.maksimov.UsersService.models.Group;

@Repository
public interface GroupsRepository extends JpaRepository<Group, Integer> {
}
