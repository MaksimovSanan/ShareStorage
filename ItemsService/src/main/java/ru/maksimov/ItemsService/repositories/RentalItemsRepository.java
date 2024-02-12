package ru.maksimov.ItemsService.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.maksimov.ItemsService.models.RentalItem;

import java.util.List;

@Repository
public interface RentalItemsRepository extends JpaRepository<RentalItem, Integer> {
    List<RentalItem> findByOwnerId(Integer ownerId);
    List<RentalItem> findByOwnerIdAndStatus(Integer ownerId, Integer status);
    List<RentalItem> findByStatus(Integer status);
    List<RentalItem> findByGroupId(Integer groupId);
}

