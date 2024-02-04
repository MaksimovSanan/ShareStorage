package ru.maksimov.webclient.models;


import lombok.*;
import org.springframework.web.multipart.MultipartFile;

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
    private byte[] avatar; // изменение типа поля на byte[]
    private String avatarBase64;
}

