package ru.maksimov.authorizationserver.services;

import ru.maksimov.authorizationserver.model.dto.RegistrationDto;

public interface UserService {
    boolean saveUser(RegistrationDto registrationDto);
    long getUserIdByEmail(String email);
}
