package ru.maksimov.imageserver.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.maksimov.imageserver.models.GroupImage;
import ru.maksimov.imageserver.services.GroupsImageService;
import ru.maksimov.imageserver.util.exceptions.GroupImageNotFoundException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Paths;

@RestController
@RequestMapping("/group-image")
public class GroupsImageController {
    private final GroupsImageService groupsImageService;

    @Autowired
    public GroupsImageController(GroupsImageService groupsImageService) {
        this.groupsImageService = groupsImageService;
    }

    @PostMapping("/{groupId}")
    public ResponseEntity<String> uploadGroupImage(@PathVariable("groupId") Integer groupId, @RequestParam("file") MultipartFile file) {
        try {
            groupsImageService.saveGroupImage(groupId, file);
            return ResponseEntity.ok("Group image uploaded successfully!");
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to upload group image.");
        }
    }

    @GetMapping("/{groupId}")
    public ResponseEntity<byte[]> getGroupImage(@PathVariable Integer groupId) {

        String pathToGroupImageDir = Paths.get("").toAbsolutePath().toString() + "/ImageServer/src/main/resources/static/groups/";
        GroupImage groupImage = groupsImageService.getGroupImage(groupId);

        File file = new File(pathToGroupImageDir + groupImage.getGroupImagePath());
        try {
            FileInputStream inputStream = new FileInputStream(file);
            byte[] imageBytes = StreamUtils.copyToByteArray(inputStream);
            return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(imageBytes);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @ExceptionHandler
    ResponseEntity<HttpStatus> handlerException(GroupImageNotFoundException groupImageNotFoundException) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
