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

//        User visitor = restTemplate.getForObject("http://USERSSERVICE/users/0?email=" + principal.getName(), User.class);
        User visitor = principalHelper.getUser(principal);

        UserInfo userInfo =restTemplate.getForObject(
                "http://AGGREGATOR/aggregator/user-info?userId=" + id + "&visitorId=" + visitor.getId(),
                UserInfo.class
        );

//         TODO UserNotFoundPage
        assert userInfo != null;

        // Получение аватара пользователя из ImageServer

        String base64Avatar = null;

        try {
            ResponseEntity<byte[]> response = restTemplate.getForEntity("http://IMAGESERVER/user-image/" + id, byte[].class);

        if (response.getStatusCode() == HttpStatus.OK) {
            byte[] avatarBytes = response.getBody();
            userInfo.getUser().setAvatar(avatarBytes);
            String avatarBase64 = Base64.getEncoder().encodeToString(avatarBytes);
            userInfo.getUser().setAvatarBase64(avatarBase64); // добавление строки Base64 в объект пользователя
            base64Avatar = Base64.getEncoder().encodeToString(userInfo.getUser().getAvatar());
        } else {
            // Обработка ситуации, когда аватар не найден
            // Например, установка дефолтного аватара или вывод ошибки
            }
        } catch(Exception e) {
            System.out.println(e);
        }




//        String base64Avatar = Base64.getEncoder().encodeToString(userInfo.getUser().getAvatar());

        // Add base64Avatar to the model
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
                           @ModelAttribute @Valid User user,
                           BindingResult result){
        System.out.println(user);
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<User> requestEntity = new HttpEntity<>(user, headers);

            restTemplate.patchForObject("http://USERSSERVICE/users/" + id, requestEntity, Void.class);
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }

        try {

            String url = "http://IMAGESERVER/user-image/" + user.getId();

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
        } catch(Exception e) {
                System.out.println(e.getMessage());
        }
        return "redirect:/user/" + id;
    }


//    public String getUserAvatar(@PathVariable Integer userId, Model model) {
//        String url = "http://your-user-image-service-url/user-image/" + userId;
//
//        // Получаем аватарку пользователя
//        ResponseEntity<byte[]> response = restTemplate.getForEntity(url, byte[].class);
//        byte[] imageBytes = response.getBody();
//
//        // Передаем данные в модель для отображения на странице
//        model.addAttribute("userId", userId);
//        model.addAttribute("image", new ByteArrayResource(imageBytes));
//
//        return "avatar-page"; // Вернуть имя представления, где будет отображаться аватарка
//    }



//    @GetMapping("/{id}/items")
//    public String getUserItems(@PathVariable("id") int id, Model model){
//        List<Item> items = Arrays.stream(restTemplate.getForObject("http://ITEMSSERVICE/items?ownerId=" + id, Item[].class)).toList();
//        model.addAttribute("userItems", items);
//        return "users/userItemsPage";
//    }
}
