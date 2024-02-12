package ru.maksimov.UsersService.dto.usersDto;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.maksimov.UsersService.models.RequestForMembership;
import ru.maksimov.UsersService.models.User;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupDTO {
    private Integer id;
    private String name;
    private String title;
    private SimpleUserDTO owner;
    private List<SimpleUserDTO> members;
    private List<RequestDTO> requests;
}
