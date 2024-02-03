package ru.maksimov.webclient.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.RestTemplate;
import ru.maksimov.webclient.models.Item;
import ru.maksimov.webclient.models.NewRentContract;
import ru.maksimov.webclient.models.NewUser;
import ru.maksimov.webclient.models.User;
import ru.maksimov.webclient.util.PrincipalHelper;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;


@Controller
public class MainController {
    private final RestTemplate restTemplate;
    private final PrincipalHelper principalHelper;

    @Autowired
    public MainController(RestTemplate restTemplate, PrincipalHelper principalHelper) {
        this.restTemplate = restTemplate;
        this.principalHelper = principalHelper;
    }

    @GetMapping
    public String getHomePage(Model model, Principal principal){
        System.out.println("USER WITH EMAIL: " + principal.getName() + " CONNECTING TO HOMEPAGE");
        User user = principalHelper.getUser(principal);
        if(user != null && "NOT FOUND".equals(user.getEmail())) {
            // REFACTOR THAT
            String addUserUrl = "http://USERSSERVICE/users";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            NewUser newUser = new NewUser(principal.getName(), principal.getName());

            HttpEntity<NewUser> requestEntity = new HttpEntity<>(newUser, headers);

            ResponseEntity<Void> responseEntity = restTemplate.postForEntity(addUserUrl, requestEntity, Void.class);

            System.out.println("USER WITH EMAIL: " + principal.getName() + " LOGIN : " + principal.getName() + " WAS ADDED TO RELATION USERS");

            user = principalHelper.getUser(principal);
        }
        model.addAttribute("user", user);
        List<Item> items = Arrays.stream(restTemplate.getForObject("http://ITEMSSERVICE/items", Item[].class)).toList();
        model.addAttribute("items", items);
        return "homePage";
    }

}
