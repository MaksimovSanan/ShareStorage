package ru.maksimov.ItemsService.dto.itemDto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewItemDTO {
    @NotNull(message = "Owner ID should not be empty")
    private Integer ownerId;

    @NotEmpty(message = "Owner name should not be empty")
    private String ownerName;

    @NotEmpty(message = "Title should not be empty")
    private String title;

    @NotEmpty(message = "Description of item cannot be empty")
    private String description;

    private Integer groupId;
    private String groupName;
}
