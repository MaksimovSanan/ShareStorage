package ru.maksimov.ItemsService.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.maksimov.ItemsService.models.RentContract;

import java.util.List;

@Repository
public interface RentContractsRepository extends JpaRepository<RentContract, Integer> {
//    List<RentContract> findByBorrowerId(Integer borrowerId);
//    List<RentContract> findAllByRentalItemOwnerId(Integer ownerId);

    @Query("SELECT rc FROM RentContract rc WHERE rc.rentalItem.ownerId = :ownerId OR rc.borrowerId = :borrowerId")
    List<RentContract> findAllByOwnerIdOrBorrowerId(@Param("ownerId") Integer ownerId, @Param("borrowerId") Integer borrowerId);

    List<RentContract> findByRentalItemId(Integer itemId);
}
