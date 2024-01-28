package ru.maksimov.UsersService.controllers;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ru.maksimov.UsersService.dto.usersDto.NewUserDTO;
import ru.maksimov.UsersService.dto.usersDto.UserDTO;
import ru.maksimov.UsersService.models.User;
import ru.maksimov.UsersService.services.UsersService;
import ru.maksimov.UsersService.util.UserErrorResponse;
import ru.maksimov.UsersService.util.exceptions.EmailIsPresentException;
import ru.maksimov.UsersService.util.exceptions.UserNotCreatedException;
import ru.maksimov.UsersService.util.exceptions.UserNotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UsersController {
    private final UsersService usersService;
    private final ModelMapper modelMapper;

    @Autowired
    public UsersController(UsersService usersService, ModelMapper modelMapper) {
        this.usersService = usersService;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public List<UserDTO> getPeople(@RequestParam(name = "email", required = false) String email) {
        return usersService.findAll().stream().map(this::convertToUserDTO).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public UserDTO findOne(@PathVariable("id") int id,
                           @RequestParam(name = "email", required = false) String email) {
        if(id == 0 && email != null) {
            Optional<User> user = usersService.findByEmail(email);
            if(user.isPresent()) {
                return convertToUserDTO(user.get());
            }
            //fix THAT
            User tmp = new User("NOT FOUND", "NOT FOUND");
            return convertToUserDTO(tmp);
        }
        return convertToUserDTO(usersService.findById(id));
    }

    @PostMapping
    public ResponseEntity<HttpStatus> create(@RequestBody @Valid NewUserDTO newUserDTO,
                                             BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            StringBuilder errorMsg = new StringBuilder();
            List<FieldError> errors = bindingResult.getFieldErrors();
            for(FieldError error : errors) {
                errorMsg.append(error.getField())
                        .append(" - ").append(error.getDefaultMessage())
                        .append(";");
            }
            throw new UserNotCreatedException(errorMsg.toString());
        }
        usersService.save(convertToUser(newUserDTO));
        return ResponseEntity.ok(HttpStatus.CREATED); // empty body status 200
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable("id") int id) {
        User user = usersService.findById(id);
        usersService.delete(user);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<HttpStatus> update(@PathVariable("id") int id, @RequestBody @Valid NewUserDTO newUserDTO,
                                             BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            StringBuilder errorMsg = new StringBuilder();
            List<FieldError> errors = bindingResult.getFieldErrors();
            for(FieldError error: errors) {
                errorMsg.append(error.getField())
                        .append(" - ").append(error.getDefaultMessage())
                        .append(";");
            }
            throw new UserNotCreatedException(errorMsg.toString());
        }
        User userToBeUpdated = usersService.findById(id);
        usersService.update(id, convertToUser(newUserDTO));
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @ExceptionHandler
    private ResponseEntity<UserErrorResponse> handleException(UserNotFoundException userNotFoundException){
        UserErrorResponse userErrorResponse = new UserErrorResponse(
                "Person with this id was not found",
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(userErrorResponse, HttpStatus.NOT_FOUND); // NOT_FOUND - 404
    }

    @ExceptionHandler
    private ResponseEntity<UserErrorResponse> handleException(UserNotCreatedException userNotCreatedException) {
        UserErrorResponse userErrorResponse = new UserErrorResponse(
                userNotCreatedException.getMessage(),
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(userErrorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    private ResponseEntity<UserErrorResponse> handleException(EmailIsPresentException emailIsPresentException) {
        UserErrorResponse userErrorResponse = new UserErrorResponse(
                "Email is present",
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(userErrorResponse, HttpStatus.BAD_REQUEST);
    }

    private User convertToUser(NewUserDTO newUserDTO) {

        return modelMapper.map(newUserDTO, User.class);
    }

    private UserDTO convertToUserDTO(User user) {
        return modelMapper.map(user, UserDTO.class);
    }

}
















