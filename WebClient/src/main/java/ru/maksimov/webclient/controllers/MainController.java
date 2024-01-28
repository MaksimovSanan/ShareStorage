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
import ru.maksimov.webclient.models.NewUser;
import ru.maksimov.webclient.models.User;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;


@Controller
public class MainController {
    private final RestTemplate restTemplate;

    @Autowired
    public MainController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping
    public String getHomePage(Model model, Principal principal){
        System.out.println("USER WITH EMAIL: " + principal.getName() + " CONNECTING TO HOMEPAGE");
        User user = restTemplate.getForObject("http://USERSSERVICE/users/0?email=" + principal.getName(), User.class);
        if(user != null && "NOT FOUND".equals(user.getEmail())) {
            // REFACTOR THAT
            String addUserUrl = "http://USERSSERVICE/users";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            NewUser newUser = new NewUser(principal.getName(), principal.getName());

            HttpEntity<NewUser> requestEntity = new HttpEntity<>(newUser, headers);

            ResponseEntity<Void> responseEntity = restTemplate.postForEntity(addUserUrl, requestEntity, Void.class);

            System.out.println("USER WITH EMAIL: " + principal.getName() + " LOGIN : " + principal.getName() + " WAS ADDED TO RELATION USERS");
        }
        List<Item> items = Arrays.stream(restTemplate.getForObject("http://ITEMSSERVICE/items", Item[].class)).toList();
        model.addAttribute("items", items);
        return "homePage";
    }

    @PostMapping("/addItem")
    public String addItem(@ModelAttribute Item newItem, Principal principal) {

        System.out.println("YA TUT");
        System.out.println(newItem);

        User user = restTemplate.getForObject("http://USERSSERVICE/users/0?email=" + principal.getName(), User.class);

        //FIX IT WITH SECURITY
        newItem.setOwnerId(user.getId());
        newItem.setOwnerName(user.getLogin());
        System.out.println("YA TUT2");
        newItem.setId(null);
        System.out.println(newItem);

        String addItemUrl = "http://ITEMSSERVICE/items";


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Item> requestEntity = new HttpEntity<>(newItem, headers);

        ResponseEntity<Void> responseEntity = restTemplate.postForEntity(addItemUrl, requestEntity, Void.class);

        System.out.println("YA TUT3");
        return "redirect:http://localhost:8081/"; // Перенаправляем пользователя на главную страницу
    }
}
