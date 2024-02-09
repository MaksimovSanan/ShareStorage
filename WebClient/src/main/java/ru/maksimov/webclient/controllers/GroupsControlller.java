package ru.maksimov.webclient.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;
import ru.maksimov.webclient.dto.UserInfo;
import ru.maksimov.webclient.models.Group;
import ru.maksimov.webclient.models.User;
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

        User user = restTemplate.getForObject("http://USERSSERVICE/users/" + visitor.getId(), User.class);
        model.addAttribute("user", user);
        model.addAttribute("groups", groups);

        return "groups/GroupsPage";
    }

    @GetMapping("/{id}")
    public String getGroupInfo(@PathVariable("id") int id, Principal principal, Model model) {
        User visitor = principalHelper.getUser(principal);
        Group group = restTemplate.getForObject("http://USERSSERVICE/groups/" + id, Group.class);
        User user = restTemplate.getForObject("http://USERSSERVICE/users/" + visitor.getId(), User.class);
        model.addAttribute("user", user);
        model.addAttribute("group", group);
        String role;
        if(user.getId().equals(group.getOwner().getId())) {
            role = "owner";
        }
        else if (user.getGroupsMember().contains(group)){
            role = "member";
        }
        else {
            role = "guest";
        }
        model.addAttribute("role", role);

        return "groups/GroupInfo";
    }
}
