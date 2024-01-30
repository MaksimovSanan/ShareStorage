package ru.maksimov.ItemsService.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.maksimov.ItemsService.models.RentContract;

import java.util.List;

@Repository
public interface RentContractsRepository extends JpaRepository<RentContract, Integer> {
    List<RentContract> findByBorrowerId(Integer borrowerId);
}
