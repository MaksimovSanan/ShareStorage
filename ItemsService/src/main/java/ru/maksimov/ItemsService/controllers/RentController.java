package ru.maksimov.ItemsService.controllers;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ru.maksimov.ItemsService.dto.rentContractDto.NewRentContractDTO;
import ru.maksimov.ItemsService.dto.rentContractDto.RentContractDTO;
import ru.maksimov.ItemsService.models.RentContract;
import ru.maksimov.ItemsService.services.RentContractsService;
import ru.maksimov.ItemsService.util.ContractErrorResponse;
import ru.maksimov.ItemsService.util.exceptions.ContractNotCreatedException;
import ru.maksimov.ItemsService.util.exceptions.ContractNotFoundException;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/rent")
public class RentController {

//    private final BooksService booksService;
//    private final PeopleService peopleService;
    private final RentContractsService rentContractsService;
    private final ModelMapper modelMapper;

    @Autowired
    public RentController(RentContractsService rentContractsService, ModelMapper modelMapper) {
        this.rentContractsService = rentContractsService;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public List<RentContractDTO> findAll(){
        return rentContractsService.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public RentContractDTO findOne(@PathVariable("id") int id) {
        return convertToDTO(rentContractsService.findById(id));
    }

    @PostMapping
    public ResponseEntity<HttpStatus> create(@RequestBody @Valid NewRentContractDTO newRentContractDTO,
                                             BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            StringBuilder errorMsg = new StringBuilder();
            List<FieldError> errors = bindingResult.getFieldErrors();
            for(FieldError error : errors) {
                errorMsg.append(error.getField())
                        .append(" - ").append(error.getDefaultMessage())
                        .append(";");
            }
            throw new ContractNotCreatedException(errorMsg.toString());
        }
        RentContract rentContract = modelMapper.map(newRentContractDTO, RentContract.class);
        rentContractsService.save(rentContract);
        return ResponseEntity.ok(HttpStatus.CREATED); // empty body status 200
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable("id") int id) {
        RentContract rentContract = rentContractsService.findById(id);
        rentContractsService.delete(rentContract);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<HttpStatus> update(@PathVariable("id") int id, @RequestBody @Valid NewRentContractDTO newRentContractDTO,
                                             BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            StringBuilder errorMsg = new StringBuilder();
            List<FieldError> errors = bindingResult.getFieldErrors();
            for(FieldError error : errors) {
                errorMsg.append(error.getField())
                        .append(" - ").append(error.getDefaultMessage())
                        .append(";");
            }
            throw new ContractNotCreatedException(errorMsg.toString());
        }

        RentContract rentContract = rentContractsService.findById(id);
        RentContract updatedRectContract = modelMapper.map(newRentContractDTO, RentContract.class);

        updatedRectContract.setId(id);
        updatedRectContract.setCreatedAt(rentContract.getCreatedAt());

        rentContractsService.save(updatedRectContract);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    private RentContractDTO convertToDTO(RentContract rentContract) {
        /* it may me method of modelMapper
        return new RentContractDTO(rentContract.getId(),
                rentContract.getBorrower().getId(),
                rentContract.getRentObject().getId(),
                rentContract.getCreatedAt(),
                rentContract.getUpdatedAt()
        );
         */

        return modelMapper.map(rentContract, RentContractDTO.class);
    }

    @ExceptionHandler
    private ResponseEntity<ContractErrorResponse> handleException(ContractNotCreatedException badRequestException){
        ContractErrorResponse contractErrorResponse = new ContractErrorResponse(
                badRequestException.getMessage(),
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(contractErrorResponse, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler
    private ResponseEntity<ContractErrorResponse> handleException(ContractNotFoundException contractNotFoundException){
        ContractErrorResponse contractErrorResponse = new ContractErrorResponse(
                "Contract with this id was not found",
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(contractErrorResponse, HttpStatus.NOT_FOUND); // NOT_FOUND - 404
    }

    @ExceptionHandler
    private ResponseEntity<ContractErrorResponse> handleException(EntityNotFoundException entityNotFoundException) {
        // TODO make it better + check borrowerId
        ContractErrorResponse contractErrorResponse = new ContractErrorResponse(
                entityNotFoundException.getMessage(),
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(contractErrorResponse, HttpStatus.BAD_REQUEST);
    }
}
