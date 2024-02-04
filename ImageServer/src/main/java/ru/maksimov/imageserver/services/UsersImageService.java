package ru.maksimov.imageserver.services;

import org.springframework.web.multipart.MultipartFile;
import ru.maksimov.imageserver.models.UserImage;

import java.io.IOException;

public interface UsersImageService {
    public void saveUserImage(Integer userId, MultipartFile file) throws IOException;
    public UserImage getUserImage(Integer userId);
}
