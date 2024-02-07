package ru.maksimov.imageserver.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.maksimov.imageserver.models.ItemImage;
import ru.maksimov.imageserver.models.UserImage;
import ru.maksimov.imageserver.services.ItemsImageService;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("item-image")
public class ItemsImageController {

    private final ItemsImageService itemsImageService;

    @Autowired
    public ItemsImageController(ItemsImageService itemsImageService) {
        this.itemsImageService = itemsImageService;
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<List<byte[]>> getUserImage(@PathVariable Integer itemId) {
        String pathToItemsImageDir = Paths.get("").toAbsolutePath().toString() + "/ImageServer/src/main/resources/static/items/";
        List<ItemImage> itemImages = itemsImageService.getItemImages(itemId);
        List<byte[]> images = new ArrayList<>();
        for(ItemImage itemImage: itemImages) {
            File file = new File( pathToItemsImageDir + itemImage.getItemImagePath());
            try {
                FileInputStream inputStream = new FileInputStream(file);
                images.add(StreamUtils.copyToByteArray(inputStream));
            } catch (IOException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }
        return ResponseEntity.ok().body(images);
    }

    @PostMapping("/{itemId}")
    public ResponseEntity<String> uploadUserImage(@PathVariable("itemId") Integer itemId,
                                                  @RequestParam("file") MultipartFile file) {
        try {
            itemsImageService.saveItemImage(itemId, file);
            return ResponseEntity.ok("User image uploaded successfully!");
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to upload user image.");
        }
    }
}
