package ru.maksimov.aggregator.models;

import lombok.*;

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
