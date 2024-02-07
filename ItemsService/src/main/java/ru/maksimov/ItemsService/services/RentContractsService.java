package ru.maksimov.ItemsService.services;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.maksimov.ItemsService.dto.rentContractDto.RentContractDTO;
import ru.maksimov.ItemsService.models.RentContract;
import ru.maksimov.ItemsService.models.RentalItem;
import ru.maksimov.ItemsService.repositories.RentContractsRepository;
import ru.maksimov.ItemsService.util.ItemCodes;
import ru.maksimov.ItemsService.util.MyHelper;
import ru.maksimov.ItemsService.util.exceptions.ContractNotFoundException;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class RentContractsService {
    private final RentContractsRepository rentContractsRepository;
    private final RentalItemsService rentalItemsService;

    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    public RentContractsService(RentContractsRepository rentContractsRepository, RentalItemsService rentalItemsService) {
        this.rentContractsRepository = rentContractsRepository;
        this.rentalItemsService = rentalItemsService;
    }

    public List<RentContract> findAll() {
        return rentContractsRepository.findAll();
    }

    public List<RentContract> findByRentalItemItemId(Integer itemId) {
        return rentContractsRepository.findByRentalItemId(itemId);
    }
    public RentContract findById(int id) {
        return rentContractsRepository.findById(id).orElseThrow(ContractNotFoundException::new);
    }

    public List<RentContract> findAllByOwnerIdOrBorrowerId(Integer borrowerId, Integer ownerId) {
        return rentContractsRepository.findAllByOwnerIdOrBorrowerId(borrowerId, ownerId);
    }

    @Transactional
    public void save(RentContract rentContract) {
        RentalItem rentalItem = rentalItemsService.findById(rentContract.getRentalItem().getId());
        if(rentalItem.getStatus() != ItemCodes.free ||
                rentalItem.getOwnerId().equals(rentContract.getBorrowerId())) {
            throw new RuntimeException("Operation not supported");
        }
        enrichContract(rentContract);
        rentContractsRepository.save(rentContract);
    }

    @Transactional
    public RentContract updateRentContract(int id, RentContractDTO rentContractUpdates) {
        RentContract existingRentContract = entityManager.find(RentContract.class, id);
        if (existingRentContract == null) {
            return null; // or throw an exception
        }

        LocalDateTime now = LocalDateTime.now();
        rentContractUpdates.setUpdatedAt(now);

        MyHelper.copyNonNullProperties(rentContractUpdates, existingRentContract);

        if(existingRentContract.getStatus() == 201) {
            existingRentContract.getRentalItem().setStatus(1);
        }
        else if(existingRentContract.getStatus() == 202) {
            existingRentContract.getRentalItem().setStatus(0);
        }

        return entityManager.merge(existingRentContract);
    }
    @Transactional
    public void delete(RentContract rentContract) {
        rentContractsRepository.delete(rentContract);
    }

    private void enrichContract(RentContract rentContract) {
        if(rentContract.getCreatedAt() == null) {
            rentContract.setCreatedAt(LocalDateTime.now());
        }
        else {
            rentContract.setUpdatedAt(LocalDateTime.now());
        }
    }
}
