package ru.maksimov.ItemsService.util;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemErrorResponse {
    private String message;
    private long timestamp;

    public ItemErrorResponse(String message, long timestamp) {
        this.message = message;
        this.timestamp = timestamp;
    }
}
