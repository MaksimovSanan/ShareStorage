package ru.maksimov.ItemsService.services;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.maksimov.ItemsService.dto.rentContractDto.RentContractDTO;
import ru.maksimov.ItemsService.models.RentContract;
import ru.maksimov.ItemsService.models.RentalItem;
import ru.maksimov.ItemsService.repositories.RentContractsRepository;
import ru.maksimov.ItemsService.util.RentCodes;
import ru.maksimov.ItemsService.util.exceptions.ContractNotFoundException;

import java.beans.PropertyDescriptor;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    public RentContract findById(int id) {
        return rentContractsRepository.findById(id).orElseThrow(ContractNotFoundException::new);
    }

    public List<RentContract> findAllByOwnerIdOrBorrowerId(Integer borrowerId, Integer ownerId) {
        return rentContractsRepository.findAllByOwnerIdOrBorrowerId(borrowerId, ownerId);
    }

    @Transactional
    public void save(RentContract rentContract) {
        enrichContract(rentContract);
        RentalItem rentalItem = rentalItemsService.findById(rentContract.getRentalItem().getId());
        if(rentContract.getStatus() == RentCodes.open) {
            rentalItem.setStatus(1);
        }
        else if(rentContract.getStatus() == RentCodes.closed) {
            rentalItem.setStatus(0);
        }
        rentContract.setRentalItem(rentalItem);
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

        copyNonNullProperties(rentContractUpdates, existingRentContract);

        existingRentContract.getRentalItem().setStatus(1);

        return entityManager.merge(existingRentContract);
    }

    private void copyNonNullProperties(Object source, Object target) {
        BeanUtils.copyProperties(source, target, getNullPropertyNames(source));
    }

    private String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        PropertyDescriptor[] pds = src.getPropertyDescriptors();
        Set<String> emptyNames = new HashSet<>();
        for (PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) emptyNames.add(pd.getName());
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
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
