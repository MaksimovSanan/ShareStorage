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
import ru.maksimov.webclient.dto.ItemInfo;
import ru.maksimov.webclient.dto.UserInfo;
import ru.maksimov.webclient.models.Item;
import ru.maksimov.webclient.models.NewRentContract;
import ru.maksimov.webclient.models.RentContract;
import ru.maksimov.webclient.models.User;
import ru.maksimov.webclient.util.PrincipalHelper;

import java.security.Principal;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/item")
public class ItemsController {
    private final RestTemplate restTemplate;
    private final PrincipalHelper principalHelper;

    @Autowired
    public ItemsController(RestTemplate restTemplate, PrincipalHelper principalHelper) {
        this.restTemplate = restTemplate;
        this.principalHelper = principalHelper;
    }

    @GetMapping("/{id}")
    public String getItemInfo(@PathVariable("id") int id, Principal principal, Model model) {

        User visitor = principalHelper.getUser(principal);

        ItemInfo itemInfo =restTemplate.getForObject(
                "http://AGGREGATOR/aggregator/item/" + id + "?visitorId=" + visitor.getId(),
                ItemInfo.class
        );

        // TODO
        // SHIT
        assert itemInfo != null;
        if(itemInfo.getRentContracts() != null) {
            itemInfo.setRentContracts(itemInfo.getRentContracts().stream().peek(RentContract::convertDateToString).collect(Collectors.toList()));
        }

        model.addAttribute("itemInfo", itemInfo);

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

        User user = restTemplate.getForObject("http://USERSSERVICE/users/0?email=" + principal.getName(), User.class);

        newItem.setOwnerId(user.getId());
        newItem.setOwnerName(user.getLogin());
        newItem.setId(null);

        String addItemUrl = "http://ITEMSSERVICE/items";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Item> requestEntity = new HttpEntity<>(newItem, headers);
        ResponseEntity<Void> responseEntity = restTemplate.postForEntity(addItemUrl, requestEntity, Void.class);

        return "redirect:/"; // Перенаправляем пользователя на главную страницу
    }

    @PatchMapping("/{id}")
    public String updateItem(@PathVariable("id") int id, @ModelAttribute Item item){
        System.out.println(item);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Item> requestEntity = new HttpEntity<>(item, headers);

        restTemplate.patchForObject("http://ITEMSSERVICE/items/" + id, requestEntity, Void.class);
        return "redirect:/";
    }

    @GetMapping("/{id}/rent")
    public String rentItemForm(@PathVariable("id") Integer itemId, Model model,
                               @ModelAttribute NewRentContract newRentContract) {
        Item item = restTemplate.getForObject("http://ITEMSSERVICE/items/" + itemId, Item.class);
        model.addAttribute("item", item);
        return "items/rentItemForm"; // Название шаблона Thymeleaf для страницы с формой аренды
    }
}
