package ru.maksimov.webclient.models;


import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Integer id;
    private String login;
    private String email;
    private String phoneNumber;
    private String introduce;
}
