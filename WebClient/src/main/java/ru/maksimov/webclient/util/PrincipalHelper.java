package ru.maksimov.webclient.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.maksimov.webclient.models.User;

import java.security.Principal;

@Component
public class PrincipalHelper {

    private final RestTemplate restTemplate;

    @Autowired
    public PrincipalHelper(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public User getUser(Principal principal) {
        return restTemplate.getForObject("http://USERSSERVICE/users/0?email=" + principal.getName(), User.class);
    }
}
