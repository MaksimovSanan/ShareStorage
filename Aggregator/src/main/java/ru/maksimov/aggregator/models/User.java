package ru.maksimov.aggregator.models;


import lombok.*;

import java.util.List;

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
    private byte[] avatar;

    private List<SimpleGroupDTO> createdGroups;

    private List<SimpleGroupDTO> GroupsMember;
    private List<RequestDTO> requestsForMembership;
}
