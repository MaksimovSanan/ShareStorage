package ru.maksimov.imageserver.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.maksimov.imageserver.models.UserImage;
import ru.maksimov.imageserver.services.UsersImageService;

import java.io.File;
import java.io.FileInputStream;
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
    public String uploadUserImage(@PathVariable("userId") Integer userId, @RequestParam("file") MultipartFile file) {
        try {
            usersImageService.saveUserImage(userId, file);
            return "User image uploaded successfully!";
        } catch (IOException e) {
            e.printStackTrace();
            return "Failed to upload user image.";
        }
    }

    @GetMapping("/{userId}")
    public ResponseEntity<byte[]> getUserImage(@PathVariable Integer userId) {
        UserImage userImage = usersImageService.getUserImage(userId);
        try {
            File file = new File(userImage.getUserImagePath());
            FileInputStream inputStream = new FileInputStream(file);
            byte[] imageBytes = StreamUtils.copyToByteArray(inputStream);
            return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(imageBytes);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }
}