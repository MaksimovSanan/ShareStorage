package ru.maksimov.webclient.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import ru.maksimov.webclient.dto.GroupInfo;
import ru.maksimov.webclient.models.*;
import ru.maksimov.webclient.util.PrincipalHelper;

import java.security.Principal;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

@Controller
@RequestMapping("/groups")
public class GroupsControlller {
    private final RestTemplate restTemplate;
    private final PrincipalHelper principalHelper;

    @Autowired
    public GroupsControlller(RestTemplate restTemplate, PrincipalHelper principalHelper) {
        this.restTemplate = restTemplate;
        this.principalHelper = principalHelper;
    }

    @GetMapping
    public String getGroupsPage(Principal principal, Model model) {
        User visitor = principalHelper.getUser(principal);

        List<Group> groups = Arrays.stream(restTemplate.getForObject(
                "http://USERSSERVICE/groups",
                Group[].class
        )).toList();

        model.addAttribute("user", visitor);
        model.addAttribute("allGroups", groups);

        return "groups/GroupsPage";
    }

    @GetMapping("/{id}")
    public String getGroupInfo(@PathVariable("id") int id, Principal principal, Model model) {
        User visitor = principalHelper.getUser(principal);
        GroupInfo groupInfo =restTemplate.getForObject(
                "http://AGGREGATOR/aggregator/group/" + id + "?visitorId=" + visitor.getId(),
                GroupInfo.class
        );

//         TODO UserNotFoundPage
        assert groupInfo != null;
        String base64Avatar;
        if(groupInfo.getGroup().getAvatar() != null) {
            base64Avatar = Base64.getEncoder().encodeToString(groupInfo.getGroup().getAvatar());
        } else {
            base64Avatar = null;
        }
        model.addAttribute("base64Avatar", base64Avatar);
        model.addAttribute("user", visitor);
        model.addAttribute("group", groupInfo.getGroup());

        model.addAttribute("role", groupInfo.getAccess());

        model.addAttribute("items", groupInfo.getItems());

        return "groups/GroupInfo";
    }


    @GetMapping("/new")
    public String newGroupForm(@ModelAttribute(name = "newGroup") NewGroup newGroup) {
        return "groups/NewGroupForm";
    }
    @PostMapping
    public String createGroup(Principal principal,
                              @ModelAttribute @Valid NewGroup newGroup) {

        User user = principalHelper.getUser(principal);

        String createGroupUrl = "http://USERSSERVICE/groups?userId=" + user.getId();


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<NewGroup> requestEntity = new HttpEntity<>(newGroup, headers);
        ResponseEntity<Group> responseEntity = restTemplate.postForEntity(createGroupUrl, requestEntity, Group.class);

        return "redirect:/groups/" + responseEntity.getBody().getId();
    }

    @PostMapping("/{id}/sendRequest")
    public String newRequestForMembership(@PathVariable("id") int id,
                                          @RequestParam(name = "message") String message,
                                          Principal principal) {
        User visitor = principalHelper.getUser(principal);
        StringBuilder requestsUrl = new StringBuilder("http://USERSSERVICE/groups/");
        requestsUrl.append(id);
        requestsUrl.append("/requests?visitorId=");
        requestsUrl.append(visitor.getId());
        if(!message.isEmpty()) {
            requestsUrl.append("&message=");
            requestsUrl.append(message);
        }
        RequestForMembership request = Arrays.stream(restTemplate.getForObject(requestsUrl.toString(), RequestForMembership[].class))
                .toList().get(0);

        return "redirect:/groups/" + id;
    }

    @PostMapping("/{groupId}/acceptRequest/{requestId}")
    public String acceptMembershipRequest(@PathVariable("groupId") int groupId,
                                          @PathVariable("requestId") int requestId,
                                          Principal principal) {
        User visitor = principalHelper.getUser(principal);
        if(visitor.getCreatedGroups().stream()
                .filter(group -> group.getId() == groupId)
                .findAny().isPresent()) {

            try {
                RequestForMembership request = restTemplate.getForObject(
                        "http://USERSSERVICE/groups/" + groupId + "/requests/" + requestId
                                + "?visitorId=" + visitor.getId(),
                        RequestForMembership.class);
            } catch(Exception e) {
                return "ServerError";
            }
            return "redirect:/groups/" + groupId;
        }
        else {
            return "PermissionDenied";
        }
    }

    @PostMapping("/{groupId}/rejectRequest/{requestId}")
    public String rejectMembershipRequest(@PathVariable("groupId") int groupId,
                                          @PathVariable("requestId") int requestId,
                                          Principal principal) {
        // Код для отклонения заявки на вступление в группу
        return "redirect:/groups/" + groupId;
    }

    @GetMapping("/{id}/edit")
    public String getEditPage(@PathVariable("id") int id, Principal principal, Model model) {
        User visitor = principalHelper.getUser(principal);
        Group group = restTemplate.getForObject("http://USERSSERVICE/groups/" + id, Group.class);
        if(group.getOwner().getId() != visitor.getId()) {
            return "errorPage";
        }

        model.addAttribute("group", group);
        return "groups/editPage";
    }

    @PatchMapping("/{id}")
    public String editUser(@PathVariable("id") int id,
                           @RequestParam("avatar") MultipartFile avatar,
                           @ModelAttribute NewGroup newGroup){
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<NewGroup> requestEntity = new HttpEntity<>(newGroup, headers);

            restTemplate.patchForObject("http://USERSSERVICE/groups/" + id, requestEntity, Void.class);
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }

        // upload avatar code absolutely like upload avatar for user, mv need to make util class for upload multipartfile
        try {
            if (avatar != null && !avatar.isEmpty()) { // Проверяем, был ли загружен новый файл аватарки
                String url = "http://IMAGESERVER/group-image/" + id;

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
                    System.out.println("Group image uploaded successfully!");
                } else {
                    System.out.println("Failed to upload group image.");
                }
            }
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
        return "redirect:/groups/" + id;
    }
}
