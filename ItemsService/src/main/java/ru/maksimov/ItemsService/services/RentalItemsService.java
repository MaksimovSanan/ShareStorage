package ru.maksimov.ItemsService.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.maksimov.ItemsService.models.RentContract;
import ru.maksimov.ItemsService.models.RentalItem;
import ru.maksimov.ItemsService.repositories.RentalItemsRepository;
import ru.maksimov.ItemsService.util.exceptions.ItemNotFoundException;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class RentalItemsService {
    private final RentalItemsRepository rentalItemsRepository;

    @Autowired
    public RentalItemsService(RentalItemsRepository rentalItemsRepository) {
        this.rentalItemsRepository = rentalItemsRepository;
    }

    public List<RentalItem> findAll() {
        return rentalItemsRepository.findAll();
    }

    public RentalItem findById(int id) {
        return rentalItemsRepository.findById(id).orElseThrow(ItemNotFoundException::new);
    }

    public List<RentalItem> findByOwnerIdAndStatus(Integer ownerId, Integer status) {
        return rentalItemsRepository.findByOwnerIdAndStatus(ownerId, status);
    }

    public List<RentalItem> findByOwnerId(Integer ownerId) {
        return rentalItemsRepository.findByOwnerId(ownerId);
    }

    public List<RentalItem> findByStatus(Integer status) {
        return rentalItemsRepository.findByStatus(status);
    }

    @Transactional
    public void save(RentalItem rentalItem) {
        enrichRentalItem(rentalItem);
        rentalItemsRepository.save(rentalItem);
    }

    @Transactional
    public void update(int id, RentalItem rentalObject) {
        rentalObject.setId(id);
        rentalItemsRepository.save(rentalObject);
    }
    @Transactional
    public void delete(RentalItem rentalObject) {
        rentalItemsRepository.delete(rentalObject);
    }
    private void enrichRentalItem(RentalItem rentalItem) {
        if(rentalItem.getStatus() == null) {
            rentalItem.setStatus(0);
        }
    }
}
