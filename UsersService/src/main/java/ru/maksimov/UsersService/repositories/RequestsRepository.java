package ru.maksimov.UsersService.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.maksimov.UsersService.models.RequestForMembership;

import java.util.List;

@Repository
public interface RequestsRepository extends JpaRepository<RequestForMembership, Integer> {
    public List<RequestForMembership> findAllByGroupId(int groupId);
    public List<RequestForMembership> findAllByUserId(int userId);
}
