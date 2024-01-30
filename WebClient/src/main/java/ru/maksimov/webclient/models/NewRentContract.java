package ru.maksimov.webclient.models;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NewRentContract {
    @NotNull(message = "RentalItem ID cannot be empty")
    private Integer rentalItemId;

    @NotNull(message = "Borrower ID cannot be empty")
    private Integer borrowerId;

    private LocalDateTime reservedTo;

    @NotNull(message = "Field reservedTo cannot be empty")
    private String reservedToFromForm;
}
