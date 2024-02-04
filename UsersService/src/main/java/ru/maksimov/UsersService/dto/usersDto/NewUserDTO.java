package ru.maksimov.UsersService.dto.usersDto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NewUserDTO {

    @NotEmpty(message = "Login should not be empty")
    @Size(min = 2, max = 30, message = "Login should be between 2 and 30 characters")
    private String login;
    @Email
    @NotEmpty(message = "Email should not be empty")
    private String email;
    private String phoneNumber;
    private String introduce;

}
