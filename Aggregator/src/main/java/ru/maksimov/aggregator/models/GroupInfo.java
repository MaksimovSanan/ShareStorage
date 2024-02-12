package ru.maksimov.aggregator.models;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupInfo {
    private String access;
    private Group group;
    private List<Item> items;
}
