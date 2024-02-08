package ru.maksimov.UsersService.dto.usersDto;

import lombok.Data;

@Data
public class MemberDTO {
    private Integer id;
    private String login;
    private String email;
    private String phoneNumber;
    private String introduce;

}
