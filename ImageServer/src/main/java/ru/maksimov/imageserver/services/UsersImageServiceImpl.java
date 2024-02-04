package ru.maksimov.imageserver.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.maksimov.imageserver.models.UserImage;
import ru.maksimov.imageserver.repositories.UsersImageRepository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class UsersImageServiceImpl implements UsersImageService{

    private final UsersImageRepository usersImageRepository;

    @Autowired
    public UsersImageServiceImpl(UsersImageRepository usersImageRepository) {
        this.usersImageRepository = usersImageRepository;
    }

    @Override
    public void saveUserImage(Integer userId, MultipartFile file) throws IOException {
        String path = Paths.get("").toAbsolutePath().toString();

        String pathToUsersImageDir = Paths.get("").toAbsolutePath().toString() + "/ImageServer/src/main/resources/static/users/";

        System.out.println(pathToUsersImageDir);

        String fileName = "user_" + userId + "_" + file.getOriginalFilename();
        String filePath = pathToUsersImageDir + fileName;

        // Сохраняем файл на диск
        File dest = new File(filePath);
        file.transferTo(dest);

        // Сохраняем путь к файлу в базу данных
        UserImage userImage = new UserImage();
        userImage.setUserId(userId);
        userImage.setUserImagePath(filePath);
        usersImageRepository.save(userImage);
    }

    @Override
    public UserImage getUserImage(Integer userId) {
        UserImage userImage = usersImageRepository.findByUserId(userId);
        return userImage;
    }
}
