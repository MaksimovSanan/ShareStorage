package ru.maksimov.authorizationserver.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.maksimov.authorizationserver.repository.UserRepository;
import ru.maksimov.authorizationserver.model.Role;
import ru.maksimov.authorizationserver.model.User;
import ru.maksimov.authorizationserver.model.dto.RegistrationDto;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public boolean saveUser(RegistrationDto registrationDto){
        if(userRepository.findByEmail(registrationDto.getEmail().toLowerCase()).isPresent()){
            System.out.println("email is present");
            return false;
        }
        User user = new User();
        user.setEmail(registrationDto.getEmail().toLowerCase());
        user.setPassword(passwordEncoder.encode(registrationDto.getPassword()));
        user.setRoles(Collections.singleton(new Role(2L, "ROLE_USER")));
        userRepository.save(user);
        return true;
    }

    @Override
    public long getUserIdByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User with username - " + email + " not found"))
                .getUserId();
    }
}
