package ru.maksimov.imageserver.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "groups_image")
@Data
@NoArgsConstructor
@Getter
@Setter
public class GroupImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_image_id")
    private Integer groupImageId;

    @Column(name = "group_id")
    private Integer groupId;

    @Column(name = "group_image_path")
    private String groupImagePath;
}
