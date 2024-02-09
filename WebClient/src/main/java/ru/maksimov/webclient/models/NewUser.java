package ru.maksimov.webclient.models;

import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NewUser {
    private String login;
    private String email;
    private String phoneNumber;
    private String introduce;



    public NewUser(String login, String email) {
        this.login = login;
        this.email = email;
    }
}
