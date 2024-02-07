package ru.maksimov.imageserver.models;


import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "items_image")
@Data
@NoArgsConstructor
@Getter
@Setter
public class ItemImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_image_id")
    private Integer itemImageId;

    @Column(name = "item_id")
    private Integer itemId;

    @Column(name = "item_image_path")
    private String itemImagePath;

}
