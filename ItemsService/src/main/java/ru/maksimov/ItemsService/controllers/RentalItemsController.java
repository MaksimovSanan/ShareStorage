package ru.maksimov.ItemsService.controllers;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ru.maksimov.ItemsService.dto.itemDto.ItemDTO;
import ru.maksimov.ItemsService.dto.itemDto.NewItemDTO;
import ru.maksimov.ItemsService.models.RentalItem;
import ru.maksimov.ItemsService.services.RentalItemsService;
import ru.maksimov.ItemsService.util.ItemErrorResponse;
import ru.maksimov.ItemsService.util.exceptions.BadRequestException;
import ru.maksimov.ItemsService.util.exceptions.ItemNotCreatedException;
import ru.maksimov.ItemsService.util.exceptions.ItemNotFoundException;


import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/items")
public class RentalItemsController {

    private final RentalItemsService rentalItemsService;
//    private final PeopleService peopleService;
    private final ModelMapper modelMapper;

    @Autowired
    public RentalItemsController(RentalItemsService rentalItemsService, ModelMapper modelMapper) {
        this.rentalItemsService = rentalItemsService;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public List<ItemDTO> getItems(@RequestParam(name = "ownerId", required = false) Integer ownerId,
                                  @RequestParam(name = "status", required = false) Integer status) {
        if (ownerId != null && status != null) {
            return rentalItemsService.findByOwnerIdAndStatus(ownerId, status)
                    .stream()
                    .map(this::convertToItemDTO)
                    .collect(Collectors.toList());
        } else if (ownerId != null) {
            return rentalItemsService.findByOwnerId(ownerId)
                    .stream()
                    .map(this::convertToItemDTO)
                    .collect(Collectors.toList());
        } else if (status != null) {
            return rentalItemsService.findByStatus(status)
                    .stream()
                    .map(this::convertToItemDTO)
                    .collect(Collectors.toList());
        } else {
            return rentalItemsService.findAll()
                    .stream()
                    .map(this::convertToItemDTO)
                    .collect(Collectors.toList());
        }
    }

    @GetMapping("/{id}")
    public ItemDTO findOne(@PathVariable("id") int id) {
        return convertToItemDTO(rentalItemsService.findById(id));
    }

    @Transactional
    @PostMapping
    public ResponseEntity<HttpStatus> create(@RequestBody @Valid NewItemDTO newItemDTO,
                                             BindingResult bindingResult) {
        System.out.println(newItemDTO);
        if(bindingResult.hasErrors()) {
            StringBuilder errorMsg = new StringBuilder();
            List<FieldError> errors = bindingResult.getFieldErrors();
            for(FieldError error: errors) {
                errorMsg.append(error.getField())
                        .append(" - ").append(error.getDefaultMessage())
                        .append(";");
                throw new ItemNotCreatedException(errorMsg.toString());
            }
        }
        RentalItem rentalItem = convertToItem(newItemDTO);
        rentalItem.setId(null);
        // how can i fix it
        rentalItemsService.save(rentalItem);
        return ResponseEntity.ok(HttpStatus.CREATED);
    }

    @Transactional
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable("id") int id) {
        RentalItem rentalItem = rentalItemsService.findById(id);
        rentalItemsService.delete(rentalItem);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @Transactional
    @PatchMapping("/{id}")
    public ResponseEntity<HttpStatus> update(@PathVariable("id") int id,
                                             @RequestBody @Valid NewItemDTO newItemDTO,
                                             BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            StringBuilder errorMsg = new StringBuilder();
            List<FieldError> errors = bindingResult.getFieldErrors();
            for(FieldError error: errors) {
                errorMsg.append(error.getField())
                        .append(" - ").append(error.getDefaultMessage())
                        .append(";");
                throw new ItemNotCreatedException(errorMsg.toString());
            }
        }
        RentalItem rentalItemToBeUpdated = rentalItemsService.findById(id);
        rentalItemToBeUpdated.setTitle(newItemDTO.getTitle());
        rentalItemToBeUpdated.setDescription(newItemDTO.getDescription());
        if(newItemDTO.getCostPerDay() != null) {
            rentalItemToBeUpdated.setCostPerDay(newItemDTO.getCostPerDay());
        }
        if(newItemDTO.getCostPerHour() != null) {
            rentalItemToBeUpdated.setCostPerHour(newItemDTO.getCostPerHour());
        }
        rentalItemsService.save(rentalItemToBeUpdated);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    private ItemDTO convertToItemDTO(RentalItem rentalItem) {
        return modelMapper.map(rentalItem, ItemDTO.class);
    }

    private RentalItem convertToItem(NewItemDTO newItemDTO) {
        return modelMapper.map(newItemDTO, RentalItem.class);
    }

    @ExceptionHandler
    private ResponseEntity<ItemErrorResponse> handleException(ItemNotFoundException itemNotFoundException) {
        ItemErrorResponse itemErrorResponse = new ItemErrorResponse(
                "Item with this id not found",
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(itemErrorResponse, HttpStatus.NOT_FOUND); // NOT_FOUND - 404
    }

    @ExceptionHandler
    private ResponseEntity<ItemErrorResponse> handleException(ItemNotCreatedException itemNotCreatedException) {
        ItemErrorResponse itemErrorResponse = new ItemErrorResponse(
                itemNotCreatedException.getMessage(),
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(itemErrorResponse, HttpStatus.BAD_REQUEST); // NOT_FOUND - 404
    }

    @ExceptionHandler
    private ResponseEntity<ItemErrorResponse> handleException(BadRequestException badRequestException){
        ItemErrorResponse itemErrorResponse = new ItemErrorResponse(
                badRequestException.getMessage(),
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(itemErrorResponse, HttpStatus.BAD_REQUEST);
    }
}
