package ru.maksimov.webclient.dto;

import lombok.*;
import ru.maksimov.webclient.models.Group;
import ru.maksimov.webclient.models.Item;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupInfo {
    private String access;
    private Group group;
    private List<Item> items;

}
