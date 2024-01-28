package ru.nova.authorizationserver.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationDto {

    @NotBlank(message = "Поле не может быть пустым")
    @Email(message = "Некорректный email")
    private String email;
//    @Pattern(regexp = "/(?=.*[0-9])(?=.*[!@#$%^&*])(?=.*[a-z])(?=.*[A-Z])[0-9a-zA-Z!@#$%^&*]{6,}/g", message = """
//            Пароль должен содержать: хотя бы одно число,
//            один спецсимвол, латинскую букву в нижнем регистре,
//            латинскую букву в верхнем регистре и состоять не менее, чем из 6 символов.
//            """)
    private String password;
    @NotBlank(message = "Поле не может быть пустым")
    private String repeatPassword;
}
