package ru.maksimov.aggregator.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import ru.maksimov.aggregator.models.Item;
import ru.maksimov.aggregator.models.RentContract;
import ru.maksimov.aggregator.models.User;
import ru.maksimov.aggregator.models.UserInfo;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/aggregator")
public class AggregatorController {
    private final RestTemplate restTemplate;

    @Autowired
    public AggregatorController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping("/user-info")
    public ResponseEntity<UserInfo> getUserInfo(@RequestParam(name = "userId") int userId,
                                                @RequestParam(name = "visitorId") int visitorId) {
        User user = restTemplate.getForObject("http://USERSSERVICE/users/" + userId, User.class);

        // TODO UserNotFoundPage
        assert user != null;

        String role;
        if(userId == visitorId) {
            role = "owner";
        } else {
            role = "guest";
        }


        List<Item> items = Arrays.stream(Objects.requireNonNull(restTemplate.getForObject(
                "http://ITEMSSERVICE/items?ownerId=" + userId, Item[].class)
        )).toList();

        List<RentContract> rentContracts = Arrays.stream(Objects.requireNonNull(restTemplate.getForObject(
                "http://ITEMSSERVICE/rent?borrowerId=" + userId + "&ownerId=" + userId, RentContract[].class)
        )).toList();


        UserInfo userInfo = new UserInfo(role, user, items, rentContracts);
        return ResponseEntity.ok(userInfo);
    }

}
