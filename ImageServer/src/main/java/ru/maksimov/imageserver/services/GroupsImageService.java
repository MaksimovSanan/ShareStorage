package ru.maksimov.imageserver.services;

import org.springframework.web.multipart.MultipartFile;
import ru.maksimov.imageserver.models.GroupImage;

import java.io.IOException;

public interface GroupsImageService {

    public void saveGroupImage(Integer groupId, MultipartFile file) throws IOException;
    public GroupImage getGroupImage(Integer groupId);
}
