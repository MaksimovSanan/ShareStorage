package ru.maksimov.imageserver.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.maksimov.imageserver.models.ItemImage;
import ru.maksimov.imageserver.models.UserImage;
import ru.maksimov.imageserver.repositories.ItemsImageRepository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Service
public class ItemsImageServiceImpl implements ItemsImageService{

    private final ItemsImageRepository itemsImageRepository;

    @Autowired
    public ItemsImageServiceImpl(ItemsImageRepository itemsImageRepository) {
        this.itemsImageRepository = itemsImageRepository;
    }

    @Override
    public void saveItemImage(Integer itemId, MultipartFile newItemFile) throws IOException {
        String pathToItemsImageDir = Paths.get("").toAbsolutePath().toString() + "/ImageServer/src/main/resources/static/items/";

        String fileName = "item_" + itemId + "_" + newItemFile.getOriginalFilename();
        String filePath = pathToItemsImageDir + fileName;

        // Сохраняем файл на диск
        File dest = new File(filePath);
        newItemFile.transferTo(dest);

        ItemImage newItemImage = new ItemImage();
        newItemImage.setItemId(itemId);
        newItemImage.setItemImagePath(fileName);
        itemsImageRepository.save(newItemImage);
    }

    @Override
    public List<ItemImage> getItemImages(Integer itemId) {
        return itemsImageRepository.findByItemId(itemId);
    }
}
