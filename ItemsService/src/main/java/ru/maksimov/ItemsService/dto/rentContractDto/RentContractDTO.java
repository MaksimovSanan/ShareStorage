package ru.maksimov.ItemsService.dto.rentContractDto;

import lombok.Getter;
import lombok.Setter;
import ru.maksimov.ItemsService.dto.itemDto.ItemDTO;

import java.time.LocalDateTime;

@Getter
@Setter
public class RentContractDTO {
    private int id;

    private ItemDTO rentalItem;

    private Integer borrowerId;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime reservedTo;
    public RentContractDTO() {
    }
}
