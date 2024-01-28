package ru.maksimov.ItemsService.util;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContractErrorResponse {

    private String message;
    private long timestamp;

    public ContractErrorResponse(String message, long timestamp) {
        this.message = message;
        this.timestamp = timestamp;
    }
}
