package ru.maksimov.UsersService.dto.usersDto;

import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;
import ru.maksimov.UsersService.models.Group;

import java.util.List;

@Getter
@Setter
public class UserDTO {

    private Integer id;
    private String login;
    private String email;
    private String phoneNumber;
    private String introduce;
    private List<Group> createdGroups;

    private List<Group> GroupsMember;
}
