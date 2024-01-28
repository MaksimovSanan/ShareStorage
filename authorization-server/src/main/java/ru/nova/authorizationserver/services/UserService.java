package ru.nova.authorizationserver.services;

import ru.nova.authorizationserver.model.dto.RegistrationDto;

public interface UserService {
    boolean saveUser(RegistrationDto registrationDto);
    long getUserIdByEmail(String email);
}
