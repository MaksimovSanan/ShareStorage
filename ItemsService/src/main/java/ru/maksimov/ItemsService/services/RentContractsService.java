package ru.maksimov.ItemsService.services;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.maksimov.ItemsService.models.RentContract;
import ru.maksimov.ItemsService.models.RentalItem;
import ru.maksimov.ItemsService.repositories.RentContractsRepository;
import ru.maksimov.ItemsService.util.exceptions.ContractNotFoundException;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class RentContractsService {
    private final RentContractsRepository rentContractsRepository;
    private final RentalItemsService rentalItemsService;
    @Autowired
    public RentContractsService(RentContractsRepository rentContractsRepository, RentalItemsService rentalItemsService) {
        this.rentContractsRepository = rentContractsRepository;
        this.rentalItemsService = rentalItemsService;
    }

    public List<RentContract> findAll() {
        return rentContractsRepository.findAll();
    }

    public RentContract findById(int id) {
        return rentContractsRepository.findById(id).orElseThrow(ContractNotFoundException::new);
    }

    public List<RentContract> findByBorrowerId(Integer borrowerId) {
        return rentContractsRepository.findByBorrowerId(borrowerId);
    }

    @Transactional
    public void save(RentContract rentContract) {
        enrichContract(rentContract);

        RentalItem rentalItem = rentalItemsService.findById(rentContract.getRentalItem().getId());
        rentalItem.setStatus(1);
        rentContract.setRentalItem(rentalItem);

        rentContractsRepository.save(rentContract);
    }

//    @Transactional
//    public void update(RentContract rentContract) {
//        rentContractsRepository.save(rentContract);
//    }
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
