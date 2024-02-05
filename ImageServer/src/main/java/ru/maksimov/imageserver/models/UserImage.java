package ru.maksimov.imageserver.models;


import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users_image")
@Data
@NoArgsConstructor
@Getter
@Setter
public class UserImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_image_id")
    private Integer userImageId;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "user_image_path")
    private String userImagePath;
}
