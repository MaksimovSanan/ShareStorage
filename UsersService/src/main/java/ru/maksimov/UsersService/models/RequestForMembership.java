package ru.maksimov.UsersService.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "requests_for_membership")
@NoArgsConstructor
@Getter
@Setter
public class RequestForMembership {
    @Id
    @Column(name = "request_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "group_id", referencedColumnName = "group_id")
    @JsonManagedReference
    private Group group;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    @JsonManagedReference
    private User user;

    @Column(name = "message")
    private String message;

    public RequestForMembership(Group group, User user, String message) {
        this.group = group;
        this.user = user;
        this.message = message;
    }
}
