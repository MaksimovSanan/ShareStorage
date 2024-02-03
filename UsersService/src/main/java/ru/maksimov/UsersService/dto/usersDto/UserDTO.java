package ru.maksimov.UsersService.dto.usersDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {

    private Integer id;
    private String login;
    private String email;
    private String phoneNumber;
    private String introduce;
}
