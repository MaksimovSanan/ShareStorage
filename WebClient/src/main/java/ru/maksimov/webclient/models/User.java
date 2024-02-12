package ru.maksimov.webclient.models;


import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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
    private byte[] avatar;

    private List<Group> createdGroups;

    private List<Group> GroupsMember;
    private List<RequestForMembership> requestsForMembership;
}

