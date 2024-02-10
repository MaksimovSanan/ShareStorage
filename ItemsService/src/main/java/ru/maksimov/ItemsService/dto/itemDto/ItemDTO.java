package ru.maksimov.ItemsService.dto.itemDto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemDTO {
    private Integer id;

    private Integer ownerId;

    private String ownerName;

    private String title;

    private String description;

    private Integer status;

    private Integer groupId;
    private String groupName;
}
