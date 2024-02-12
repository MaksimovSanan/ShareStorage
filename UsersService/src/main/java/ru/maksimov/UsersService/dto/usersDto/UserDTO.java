package ru.maksimov.UsersService.dto.usersDto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserDTO {

    private Integer id;
    private String login;
    private String email;
    private String phoneNumber;
    private String introduce;
    private List<SimpleGroupDTO> createdGroups;

    private List<SimpleGroupDTO> GroupsMember;
    private List<RequestDTO> requestsForMembership;
}
