package ru.maksimov.ItemsService.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.maksimov.ItemsService.util.ContractErrorResponse;
import ru.maksimov.ItemsService.util.exceptions.ContractNotCreatedException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ContractNotCreatedException.class)
    public ResponseEntity<ContractErrorResponse> handleContractNotCreatedException(ContractNotCreatedException ex) {
        ContractErrorResponse contractErrorResponse = new ContractErrorResponse(ex.getMessage(), System.currentTimeMillis());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(contractErrorResponse);
    }
}
