package ru.maksimov.webclient.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import ru.maksimov.webclient.models.Item;
import ru.maksimov.webclient.models.NewUser;
import ru.maksimov.webclient.models.User;
import ru.maksimov.webclient.dto.UserInfo;
import ru.maksimov.webclient.util.PrincipalHelper;

import java.security.Principal;
import java.util.Arrays;
import java.util.Base64;
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

        User visitor = principalHelper.getUser(principal);

        UserInfo userInfo =restTemplate.getForObject(
                "http://AGGREGATOR/aggregator/user/" + id + "?visitorId=" + visitor.getId(),
                UserInfo.class
        );

//         TODO UserNotFoundPage
        assert userInfo != null;

        String base64Avatar;
        if(userInfo.getUser().getAvatar() != null) {
            base64Avatar = Base64.getEncoder().encodeToString(userInfo.getUser().getAvatar());
        } else {
            base64Avatar = null;
        }
        model.addAttribute("base64Avatar", base64Avatar);
        model.addAttribute("userInfo", userInfo);
        return "users/userInfo";
    }

    @GetMapping("/{id}/edit")
    public String getEditPage(@PathVariable("id") int id, Principal principal, Model model) {
        User visitor = principalHelper.getUser(principal);
        if(id != visitor.getId()) {
            return "errorPage";
        }

        User user = restTemplate.getForObject("http://USERSSERVICE/users/" + id, User.class);
        model.addAttribute("user", user);
        return "users/editPage";
    }

    @PatchMapping("/{id}")
    public String editUser(@PathVariable("id") int id,
                           @RequestParam("avatar") MultipartFile avatar,
                           @ModelAttribute NewUser user){
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<NewUser> requestEntity = new HttpEntity<>(user, headers);

            restTemplate.patchForObject("http://USERSSERVICE/users/" + id, requestEntity, Void.class);
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }

        try {
            if (avatar != null && !avatar.isEmpty()) { // Проверяем, был ли загружен новый файл аватарки
                String url = "http://IMAGESERVER/user-image/" + id;

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.MULTIPART_FORM_DATA);

                MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
                body.add("file", avatar.getResource());

                HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

                ResponseEntity<String> response = restTemplate.exchange(
                        url,
                        HttpMethod.POST,
                        requestEntity,
                        String.class);

                if (response.getStatusCode() == HttpStatus.OK) {
                    System.out.println("User image uploaded successfully!");
                } else {
                    System.out.println("Failed to upload user image.");
                }
            }
        } catch(Exception e) {
                System.out.println(e.getMessage());
        }
        return "redirect:/user/" + id;
    }



//    @GetMapping("/{id}/items")
//    public String getUserItems(@PathVariable("id") int id, Model model){
//        List<Item> items = Arrays.stream(restTemplate.getForObject("http://ITEMSSERVICE/items?ownerId=" + id, Item[].class)).toList();
//        model.addAttribute("userItems", items);
//        return "users/userItemsPage";
//    }
}
