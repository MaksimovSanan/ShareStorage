package ru.maksimov.imageserver.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.multipart.MultipartFile;
import ru.maksimov.imageserver.models.UserImage;
import ru.maksimov.imageserver.services.UsersImageService;
import ru.maksimov.imageserver.util.exceptions.UserImageNotFoundException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

@RestController
@RequestMapping("user-image")
public class UsersImageController {
    private final UsersImageService usersImageService;

    @Autowired
    public UsersImageController(UsersImageService usersImageService) {
        this.usersImageService = usersImageService;
    }

    @PostMapping("/{userId}")
    public ResponseEntity<String> uploadUserImage(@PathVariable("userId") Integer userId, @RequestParam("file") MultipartFile file) {
        try {
            usersImageService.saveUserImage(userId, file);
            return ResponseEntity.ok("User image uploaded successfully!");
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to upload user image.");
        }
    }

    @GetMapping("/{userId}")
    public ResponseEntity<byte[]> getUserImage(@PathVariable Integer userId) {
        UserImage userImage = usersImageService.getUserImage(userId);

        File file = new File(userImage.getUserImagePath());
        try {
            FileInputStream inputStream = new FileInputStream(file);
            byte[] imageBytes = StreamUtils.copyToByteArray(inputStream);
            return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(imageBytes);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @ExceptionHandler
    ResponseEntity<HttpStatus> handlerException(UserImageNotFoundException userImageNotFoundException) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
