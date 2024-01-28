package ru.maksimov.webclient.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import ru.maksimov.webclient.models.Item;
import ru.maksimov.webclient.models.User;

import java.security.Principal;

@Controller
@RequestMapping("/item")
public class ItemsController {
    private final RestTemplate restTemplate;

    @Autowired
    public ItemsController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping("/{id}")
    public String getItemInfo(@PathVariable("id") int id, Model model) {
        Item item = restTemplate.getForObject("http://ITEMSSERVICE/items/" + id, Item.class);
        model.addAttribute("item", item);
        return "items/itemInfo";
    }

    @DeleteMapping("/{id}")
    public String deleteItem(@PathVariable("id") int id) {
        restTemplate.delete("http://ITEMSSERVICE/items/" + id);
        return "redirect:http://localhost:8080/";
    }

    @GetMapping("/{id}/edit")
    public String editPage(@PathVariable("id") int id, Model model) {
        Item item = restTemplate.getForObject("http://ITEMSSERVICE/items/" + id, Item.class);
        System.out.println(item);
        model.addAttribute("item", item);
        return "items/editPage";
    }

    @GetMapping("/new")
    public String newItemPage(@ModelAttribute("item") Item item) {
        return "items/newItemPage";
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
        return "redirect:/"; // Перенаправляем пользователя на главную страницу
    }

    @PatchMapping("/{id}")
    public String updateItem(@PathVariable("id") int id, @ModelAttribute Item item){
        System.out.println(item);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Item> requestEntity = new HttpEntity<>(item, headers);

        restTemplate.patchForObject("http://ITEMSSERVICE/items/" + id, requestEntity, Void.class);
        return "redirect:http://localhost:8080/";
    }
}
