package ru.maksimov.aggregator.models;

import lombok.*;

import java.util.List;


@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Item {
    private Integer id;

    private Integer ownerId;

    private String ownerName;

    private String title;

    private String description;

    private Integer status;

    private Integer groupId;

    private String groupName;

    private List<byte[]> pictures;

}
