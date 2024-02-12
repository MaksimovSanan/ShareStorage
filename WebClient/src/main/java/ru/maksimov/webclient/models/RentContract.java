package ru.maksimov.webclient.models;

import lombok.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RentContract {
    private Integer id;
    private Item rentalItem;
    private Integer borrowerId;
    private String borrowerName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime reservedFrom;
    private LocalDateTime reservedTo;
    private String comment;
    private Integer status;

    private String reservedFromDateFromForm;
    private String reservedFromTimeFromForm;
    private String reservedToDateFromForm;
    private String reservedToTimeFromForm;


    public void convertDateToString() {
        if(reservedFrom != null && reservedTo != null) {
            this.reservedFromDateFromForm = reservedFrom.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            this.reservedFromTimeFromForm = reservedFrom.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
            this.reservedToDateFromForm = reservedTo.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            this.reservedToTimeFromForm = reservedTo.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        }
    }
}
