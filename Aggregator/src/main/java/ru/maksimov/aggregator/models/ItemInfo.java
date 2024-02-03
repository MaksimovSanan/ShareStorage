package ru.maksimov.aggregator.models;

import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ItemInfo {
    private String access;
    private Item item;
    private List<RentContract> rentContracts;
}
