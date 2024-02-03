package ru.maksimov.aggregator.models;

import lombok.*;

import java.time.LocalDateTime;

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
}
