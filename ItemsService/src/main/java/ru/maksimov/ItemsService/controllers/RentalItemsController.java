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
import ru.maksimov.ItemsService.util.MyHelper;
import ru.maksimov.ItemsService.util.exceptions.ItemNotFoundException;


import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/items")
public class RentalItemsController {

    private final RentalItemsService rentalItemsService;
    private final ModelMapper modelMapper;

    @Autowired
    public RentalItemsController(RentalItemsService rentalItemsService, ModelMapper modelMapper) {
        this.rentalItemsService = rentalItemsService;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public ResponseEntity<List<ItemDTO>> getItems(@RequestParam(name = "ownerId", required = false) Integer ownerId,
                                  @RequestParam(name = "status", required = false) Integer status,
                                                  @RequestParam(name = "groupId", required = false) Integer groupId) {
        List<RentalItem> items;
        if(groupId != null) {
            items = rentalItemsService.findByGroupId(groupId);
        } else if (ownerId != null && status != null) {
            items = rentalItemsService.findByOwnerIdAndStatus(ownerId, status);
        } else if (ownerId != null) {
            items = rentalItemsService.findByOwnerId(ownerId);
        } else if (status != null) {
            items = rentalItemsService.findByStatus(status);
        } else {
            items = rentalItemsService.findAll();
        }
        return ResponseEntity.ok(items.stream().map(this::convertToItemDTO).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemDTO> findOne(@PathVariable("id") int id) {
        return ResponseEntity.ok(convertToItemDTO(rentalItemsService.findById(id)));
    }


    @Transactional
    @PostMapping
    public ResponseEntity<String> create(@RequestBody @Valid NewItemDTO newItemDTO,
                                             BindingResult bindingResult) {

        String errMsg = MyHelper.handlingBindingResult(bindingResult);
        if(!errMsg.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(errMsg);
        }

        RentalItem rentalItem = convertToItem(newItemDTO);

        System.out.println(rentalItem.getId());
//        how can i fix it
        rentalItem.setId(null);

        try {
            rentalItemsService.save(rentalItem);
        } catch(Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error occurred while creating item: " + e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.CREATED).body("Item created successfully");
    }

    @Transactional
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") int id) {
        RentalItem rentalItem = rentalItemsService.findById(id);
        try{
            rentalItemsService.delete(rentalItem);
        } catch(Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error occurred while deleting item:" + e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.OK).body("item was deleted");
    }

    @Transactional
    @PatchMapping("/{id}")
    public ResponseEntity<ItemDTO> update(@PathVariable("id") int id,
                                             @RequestBody ItemDTO itemDTO) {

        RentalItem updatedRentalItem = rentalItemsService.update(id, itemDTO);
        if (updatedRentalItem == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(convertToItemDTO(updatedRentalItem));
    }




    private ItemDTO convertToItemDTO(RentalItem rentalItem) {
        return modelMapper.map(rentalItem, ItemDTO.class);
    }

    private RentalItem convertToItem(NewItemDTO newItemDTO) {
        return modelMapper.map(newItemDTO, RentalItem.class);
    }

    @ExceptionHandler
    private ResponseEntity<HttpStatus> handleException(ItemNotFoundException itemNotFoundException) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

}
