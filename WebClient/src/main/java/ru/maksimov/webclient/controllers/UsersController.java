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

import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UsersController {
    private final RestTemplate restTemplate;

    @Autowired
    public UsersController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping("/{id}")
    public String getUserInfo(@PathVariable("id") int id, Model model){
        User user = restTemplate.getForObject("http://USERSSERVICE/users/" + id, User.class);
        model.addAttribute("user", user);
        List<Item> items = Arrays.stream(restTemplate.getForObject("http://ITEMSSERVICE/items?ownerId=" + id, Item[].class)).toList();
        model.addAttribute("userItems", items);
        List<RentContract> rentContracts = Arrays.stream(restTemplate.getForObject("http://ITEMSSERVICE/rent?borrowerId=" + id, RentContract[].class)).toList();
        model.addAttribute("rentContracts", rentContracts);
        return "users/userInfo";
    }

    @GetMapping("/{id}/items")
    public String getUserItems(@PathVariable("id") int id, Model model){
        List<Item> items = Arrays.stream(restTemplate.getForObject("http://ITEMSSERVICE/items?ownerId=" + id, Item[].class)).toList();
        model.addAttribute("userItems", items);
        return "users/userItemsPage";
    }
}
