package ru.maksimov.UsersService.dto.usersDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SimpleUserDTO {
    private Integer id;
    private String login;
    private String email;
    private String phoneNumber;
    private String introduce;
}
