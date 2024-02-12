package ru.maksimov.UsersService.models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "users")
@NoArgsConstructor
@Getter
@Setter
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer id;

    @Column(name = "login")
    @NotEmpty(message = "Login should not be empty")
    @Size(min = 2, max = 30, message = "Login should be between 2 and 30 characters")
    private String login;

    @Column(name = "email")
    @Email
    @NotEmpty(message = "Email should not be empty")
    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "introduce")
    private String introduce;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "owner")
    private List<Group> createdGroups;

    @ManyToMany(mappedBy = "members")
    private List<Group> groupsMember;

    @OneToMany(mappedBy = "user")
    private List<RequestForMembership> requestsForMembership;

    public User(String login,String email) {
        this.login = login;
        this.email = email;
    }

    public void addGroup(Group group) {
        if(groupsMember == null) {
            groupsMember = new ArrayList<>();
        }
        groupsMember.add(group);
    }
}
