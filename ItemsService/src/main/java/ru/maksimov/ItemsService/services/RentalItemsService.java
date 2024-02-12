package ru.maksimov.ItemsService.services;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.maksimov.ItemsService.dto.itemDto.ItemDTO;
import ru.maksimov.ItemsService.models.RentalItem;
import ru.maksimov.ItemsService.repositories.RentalItemsRepository;
import ru.maksimov.ItemsService.util.MyHelper;
import ru.maksimov.ItemsService.util.exceptions.ItemNotFoundException;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class RentalItemsService {
    private final RentalItemsRepository rentalItemsRepository;
    @PersistenceContext
    private EntityManager entityManager;

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

    public List<RentalItem> findByGroupId(Integer groupId) {
        return rentalItemsRepository.findByGroupId(groupId);
    }

    @Transactional
    public void save(RentalItem rentalItem) {
        enrichRentalItem(rentalItem);
        rentalItemsRepository.save(rentalItem);
    }

    @Transactional
    public RentalItem update(int id, ItemDTO rentalItemUpdates) {
        RentalItem existingRentalItem = entityManager.find(RentalItem.class, id);
        if (existingRentalItem == null) {
            return null; // or throw an exception
        }

        MyHelper.copyNonNullProperties(rentalItemUpdates, existingRentalItem);

        return entityManager.merge(existingRentalItem);
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
