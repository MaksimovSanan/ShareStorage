package ru.maksimov.UsersService.dto.usersDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestDTO {
    private Integer id;
    private Integer groupId;
    private Integer userId;
    private String message;
}
