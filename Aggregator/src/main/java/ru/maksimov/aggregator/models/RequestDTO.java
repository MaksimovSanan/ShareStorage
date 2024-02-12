package ru.maksimov.aggregator.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestDTO {
    private Integer id;
    private Integer groupId;
    private Integer userId;
    private String message;
}
