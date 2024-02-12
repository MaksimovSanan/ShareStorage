package ru.maksimov.ItemsService.dto.rentContractDto;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import ru.maksimov.ItemsService.models.RentalItem;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class NewRentContractDTO {

    @NotNull(message = "RentalItem ID cannot be empty")
    private RentalItem rentalItemId;

    @NotNull(message = "Borrower ID cannot be empty")
    private Integer borrowerId;

    @NotNull(message = "Borrower name cannot be empty")
    private String borrowerName;

    private LocalDateTime reservedFrom;

    private LocalDateTime reservedTo;

    private String comment;

    @NotNull(message = "Status cannot be empty")
    private Integer status;
}
