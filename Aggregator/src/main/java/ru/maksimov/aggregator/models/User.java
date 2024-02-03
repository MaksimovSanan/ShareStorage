package ru.maksimov.aggregator.models;


import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private int id;
    private String login;
    private String email;
    private String phoneNumber;
    private String introduce;
}
