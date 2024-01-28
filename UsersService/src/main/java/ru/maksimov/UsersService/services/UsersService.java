package ru.maksimov.UsersService.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.maksimov.UsersService.models.User;
import ru.maksimov.UsersService.repositories.UsersRepository;
import ru.maksimov.UsersService.util.exceptions.EmailIsPresentException;
import ru.maksimov.UsersService.util.exceptions.UserNotFoundException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class UsersService {

    private final UsersRepository usersRepository;

    @Autowired
    public UsersService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    public List<User> findAll() {
        return usersRepository.findAll();
    }

    public User findById(int id) {
        Optional<User> person = usersRepository.findById(id);
        return person.orElseThrow(UserNotFoundException::new);
    }
    
    public Optional<User> findByEmail(String email) {
        return usersRepository.findByEmail(email);
    }

    @Transactional
    public void save(User user) {
        if(usersRepository.findByEmail(user.getEmail()).isPresent()){
            throw new EmailIsPresentException();
        }
        enrichUser(user);
        usersRepository.save(user);
    }

    @Transactional
    public void update(int id, User user) {
        user.setId(id);
        Optional<User> tmpUser = usersRepository.findByEmail(user.getEmail());
        if(tmpUser.isPresent() && tmpUser.get().getId() != id){
            throw new EmailIsPresentException();
        }
        usersRepository.save(user);
    }

    @Transactional
    public void delete(User user) {
        usersRepository.delete(user);
    }

    private void enrichUser(User user) {
        user.setCreatedAt(LocalDateTime.now());
    }
}
