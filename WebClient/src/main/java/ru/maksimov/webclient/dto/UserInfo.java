package ru.maksimov.webclient.dto;

import lombok.*;
import ru.maksimov.webclient.models.Item;
import ru.maksimov.webclient.models.RentContract;
import ru.maksimov.webclient.models.User;

import java.util.List;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserInfo {
    private String access;
    private User user;
    private List<Item> userItems;
    private List<RentContract> userRentContracts;
}
