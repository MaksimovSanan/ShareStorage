package ru.maksimov.webclient.models;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RentContract {
    private int id;

    private Item rentalItem;

    private Integer borrowerId;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime reservedTo;
}
