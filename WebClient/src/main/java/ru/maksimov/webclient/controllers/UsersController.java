package ru.maksimov.webclient.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;
import ru.maksimov.webclient.models.Item;
import ru.maksimov.webclient.models.User;
import ru.maksimov.webclient.dto.UserInfo;
import ru.maksimov.webclient.util.PrincipalHelper;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UsersController {
    private final RestTemplate restTemplate;
    private final PrincipalHelper principalHelper;

    @Autowired
    public UsersController(RestTemplate restTemplate, PrincipalHelper principalHelper) {
        this.restTemplate = restTemplate;
        this.principalHelper = principalHelper;
    }

    @GetMapping("/{id}")
    public String getUserInfo(@PathVariable("id") int id, Model model, Principal principal){

//        User visitor = restTemplate.getForObject("http://USERSSERVICE/users/0?email=" + principal.getName(), User.class);
        User visitor = principalHelper.getUser(principal);

        UserInfo userInfo =restTemplate.getForObject(
                "http://AGGREGATOR/aggregator/user-info?userId=" + id + "&visitorId=" + visitor.getId(),
                UserInfo.class
        );


//         TODO UserNotFoundPage
        assert userInfo != null;
        model.addAttribute("userInfo", userInfo);
        return "users/userInfo";
    }

    @GetMapping("/{id}/items")
    public String getUserItems(@PathVariable("id") int id, Model model){
        List<Item> items = Arrays.stream(restTemplate.getForObject("http://ITEMSSERVICE/items?ownerId=" + id, Item[].class)).toList();
        model.addAttribute("userItems", items);
        return "users/userItemsPage";
    }
}
