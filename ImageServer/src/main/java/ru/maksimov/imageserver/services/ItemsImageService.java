package ru.maksimov.imageserver.services;

import org.springframework.web.multipart.MultipartFile;
import ru.maksimov.imageserver.models.ItemImage;
import ru.maksimov.imageserver.models.UserImage;

import java.io.IOException;
import java.util.List;

public interface ItemsImageService {
    public void saveItemImage(Integer itemId, MultipartFile file) throws IOException;
    public List<ItemImage> getItemImages(Integer itemId);
}
