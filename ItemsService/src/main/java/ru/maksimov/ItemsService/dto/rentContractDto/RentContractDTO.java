package ru.maksimov.ItemsService.dto.rentContractDto;

import lombok.*;
import ru.maksimov.ItemsService.dto.itemDto.ItemDTO;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RentContractDTO {
    private Integer id;
    private ItemDTO rentalItem;
    private Integer borrowerId;
    private String borrowerName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime reservedFrom;
    private LocalDateTime reservedTo;
    private String comment;
    private Integer status;
}
