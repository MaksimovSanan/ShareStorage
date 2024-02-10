package ru.maksimov.webclient.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import ru.maksimov.webclient.dto.UserInfo;
import ru.maksimov.webclient.models.*;
import ru.maksimov.webclient.util.PrincipalHelper;

import java.security.Principal;
import java.util.Arrays;
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
        model.addAttribute("groups", groups);

        return "groups/GroupsPage";
    }

    @GetMapping("/{id}")
    public String getGroupInfo(@PathVariable("id") int id, Principal principal, Model model) {
        User visitor = principalHelper.getUser(principal);
        Group group = restTemplate.getForObject("http://USERSSERVICE/groups/" + id, Group.class);
        model.addAttribute("user", visitor);
        model.addAttribute("group", group);
        String role;
        if(visitor.getId().equals(group.getOwner().getId())) {
            role = "owner";
        }
        else if (visitor.getGroupsMember().contains(group)){
            role = "member";
        }
        else if (visitor.getRequestsForMembership().stream()
                .filter(requestForMembership -> requestForMembership.getGroupId().equals(group.getId()))
                .findAny().isPresent()) {
            role = "guestWithRequest";
        }
        else {
            role = "guest";
        }
        model.addAttribute("role", role);

        List<Item> items = Arrays.stream(restTemplate.getForObject("http://ITEMSSERVICE/items?groupId=" + id, Item[].class))
                .toList();

        model.addAttribute("items", items);

        model.addAttribute("user", visitor);
        return "groups/GroupInfo";
    }


    @GetMapping("/new")
    public String newGroupForm(@ModelAttribute(name = "newGroup") NewGroup newGroup) {
        return "groups/newGroupForm";
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
}
