package ru.maksimov.ItemsService.dto.itemDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemDTO {
    private Integer id;

    private Integer ownerId;

    private String ownerName;

    private String title;

    private String description;

    private Integer status;

    private Integer costPerHour;

    private Integer costPerDay;
}
