package ru.maksimov.webclient.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;
import ru.maksimov.webclient.models.Item;
import ru.maksimov.webclient.models.RentContract;
import ru.maksimov.webclient.models.User;
import ru.maksimov.webclient.models.UserInfo;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/user")
public class UsersController {
    private final RestTemplate restTemplate;

    @Autowired
    public UsersController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping("/{id}")
    public String getUserInfo(@PathVariable("id") int id, Model model, Principal principal){

        User visitor = restTemplate.getForObject("http://USERSSERVICE/users/0?email=" + principal.getName(), User.class);

        UserInfo userInfo =restTemplate.getForObject(
                "http://AGGREGATOR/aggregator/user-info?userId=" + id + "&visitorId=" + visitor.getId(),
                UserInfo.class
        );

//        User user = restTemplate.getForObject("http://USERSSERVICE/users/" + id, User.class);
//        model.addAttribute("user", user);
        model.addAttribute("user", userInfo.getUser());

//         TODO UserNotFoundPage
//        assert user != null;

//        if(user.getEmail().equals(principal.getName())) {
//            model.addAttribute("role", "owner");
//        } else {
//            model.addAttribute("role", "guest");
//        }

        model.addAttribute("role", userInfo.getRole());


//        List<Item> items = Arrays.stream(Objects.requireNonNull(restTemplate.getForObject(
//                "http://ITEMSSERVICE/items?ownerId=" + id, Item[].class)
//        )).toList();
//        model.addAttribute("userItems", items);

        model.addAttribute("userItems", userInfo.getUserItems());

//        List<RentContract> rentContracts = Arrays.stream(Objects.requireNonNull(restTemplate.getForObject(
//                "http://ITEMSSERVICE/rent?borrowerId=" + id + "&ownerId=" + id, RentContract[].class)
//        )).toList();
//        model.addAttribute("rentContracts", rentContracts);

        model.addAttribute("rentContracts", userInfo.getUserRentContracts());
        return "users/userInfo";
    }

    @GetMapping("/{id}/items")
    public String getUserItems(@PathVariable("id") int id, Model model){
        List<Item> items = Arrays.stream(restTemplate.getForObject("http://ITEMSSERVICE/items?ownerId=" + id, Item[].class)).toList();
        model.addAttribute("userItems", items);
        return "users/userItemsPage";
    }
}
