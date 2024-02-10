package ru.maksimov.aggregator.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import ru.maksimov.aggregator.models.*;
import ru.maksimov.aggregator.services.AggregatorService;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/aggregator")
public class AggregatorController {
    private final AggregatorService aggregatorService;

    @Autowired
    public AggregatorController(AggregatorService aggregatorService) {
        this.aggregatorService = aggregatorService;
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<UserInfo> getUserInfo(@PathVariable("id") int userId,
                                                @RequestParam(name = "visitorId") int visitorId) {

        UserInfo userInfo = aggregatorService.getUserInfo(userId, visitorId);
        if(userInfo == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(userInfo);
    }

    @GetMapping("item/{id}")
    public ResponseEntity<ItemInfo> getItemInfo(@PathVariable("id") int itemId,
                                            @RequestParam(name = "visitorId") int visitorId) {

        ItemInfo itemInfo = aggregatorService.getItemInfo(itemId, visitorId);
        if(itemInfo == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(itemInfo);
    }
}
