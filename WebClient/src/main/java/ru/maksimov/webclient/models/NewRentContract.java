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
    private Integer borrowerId;
    private String borrowerName;
    private LocalDateTime reservedFrom;
    private LocalDateTime reservedTo;
    private String comment;
    @NotNull(message = "Status cannot be empty")
    private Integer status;

    private String reservedFromDateFromForm;
    private String reservedFromTimeFromForm;
    private String reservedToDateFromForm;
    private String reservedToTimeFromForm;

}
