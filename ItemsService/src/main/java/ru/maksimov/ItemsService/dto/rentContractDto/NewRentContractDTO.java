package ru.maksimov.ItemsService.dto.rentContractDto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import ru.maksimov.ItemsService.models.RentalItem;

import java.time.LocalDateTime;

@Getter
@Setter
public class NewRentContractDTO {

    @NotNull(message = "RentalItem ID cannot be empty")
    private RentalItem rentalItemId;

    @NotNull(message = "Borrower ID cannot be empty")
    private Integer borrowerId;

    @NotNull(message = "Field reservedTo cannot be empty")
    private LocalDateTime reservedTo;

    public NewRentContractDTO() {
    }
}
