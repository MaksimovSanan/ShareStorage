package ru.maksimov.webclient.models;

import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NewUser {
    private String login;
    private String email;
}
