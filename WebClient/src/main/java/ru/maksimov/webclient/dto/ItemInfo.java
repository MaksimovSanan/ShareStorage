package ru.maksimov.webclient.dto;

import lombok.*;
import ru.maksimov.webclient.models.Item;
import ru.maksimov.webclient.models.RentContract;

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
