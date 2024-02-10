package ru.maksimov.aggregator.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SimpleGroupDTO {
    private Integer id;
    private String name;
    private String title;
}
