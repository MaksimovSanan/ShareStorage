package ru.maksimov.UsersService.dto.usersDto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class NewGroupDTO {
    @NotEmpty(message = "Name of group should not be empty")
    private String name;

    private String title;
}
