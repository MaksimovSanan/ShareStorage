package ru.maksimov.webclient.models;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewGroup {
    @NotEmpty(message = "Name of group should not be empty")
    private String name;

    private String title;
}
