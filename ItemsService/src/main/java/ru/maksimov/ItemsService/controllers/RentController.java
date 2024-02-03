package ru.maksimov.ItemsService.controllers;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.maksimov.ItemsService.dto.rentContractDto.NewRentContractDTO;
import ru.maksimov.ItemsService.dto.rentContractDto.RentContractDTO;
import ru.maksimov.ItemsService.models.RentContract;
import ru.maksimov.ItemsService.services.RentContractsService;
import ru.maksimov.ItemsService.util.MyHelper;
import ru.maksimov.ItemsService.util.exceptions.ContractNotFoundException;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/rent")
public class RentController {

    private final RentContractsService rentContractsService;
    private final ModelMapper modelMapper;

    @Autowired
    public RentController(RentContractsService rentContractsService, ModelMapper modelMapper) {
        this.rentContractsService = rentContractsService;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public ResponseEntity<List<RentContractDTO>> findAll(
            @RequestParam(name = "itemId", required = false) Integer itemId,
            @RequestParam(name = "borrowerId", required = false) Integer borrowerId,
            @RequestParam(name = "ownerId", required = false) Integer ownerId) {

        List<RentContract> rentContracts;

        if (borrowerId != null || ownerId != null) {
            rentContracts = rentContractsService.findAllByOwnerIdOrBorrowerId(borrowerId, ownerId);
        } else if (itemId != null){
            rentContracts = rentContractsService.findByRentalItemItemId(itemId);
        } else {
            rentContracts = rentContractsService.findAll();
        }

//        if (rentContracts.isEmpty()) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
//        } else {
            return ResponseEntity.ok(
                    rentContracts.stream()
                            .map(this::convertToDTO)
                            .collect(Collectors.toList())
                    );
//        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<RentContractDTO> findOne(@PathVariable("id") int id) {
        RentContract rentContract = rentContractsService.findById(id);
        if(rentContract != null) {
            return ResponseEntity.ok(convertToDTO(rentContract));
        }
        else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .build();
        }
    }

    @PostMapping
    public ResponseEntity<String> create(@RequestBody @Valid NewRentContractDTO newRentContractDTO,
                                             BindingResult bindingResult) {

        String errMsg = MyHelper.handlingBindingResult(bindingResult);
        if(!errMsg.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(errMsg);
        }
        RentContract rentContract = modelMapper.map(newRentContractDTO, RentContract.class);

        //        how can i fix it
        rentContract.setId(null);

        try {
            rentContractsService.save(rentContract);
        } catch(Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error occurred while creating rentContract:" + e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.CREATED).body("Contract created successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") int id) {
        RentContract rentContract = rentContractsService.findById(id);
        try{
            rentContractsService.delete(rentContract);
        } catch(Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error occurred while deleting contract:" + e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.OK).body("Contract was deleted");
    }

    @PatchMapping("/{id}")
    public ResponseEntity<RentContractDTO> updateRentContract(@PathVariable("id") int id,
                                                           @RequestBody RentContractDTO rentContractUpdates) {

        RentContract updatedRentContract = rentContractsService.updateRentContract(id, rentContractUpdates);
        if (updatedRentContract == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(convertToDTO(updatedRentContract));
    }




    private RentContractDTO convertToDTO(RentContract rentContract) {
        return modelMapper.map(rentContract, RentContractDTO.class);
    }

    @ExceptionHandler
    private ResponseEntity<HttpStatus> handleException(ContractNotFoundException contractNotFoundException){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

}
