package ru.maksimov.imageserver.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.maksimov.imageserver.models.UserImage;
import ru.maksimov.imageserver.repositories.UsersImageRepository;
import ru.maksimov.imageserver.util.exceptions.UserImageNotFoundException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@Service
public class UsersImageServiceImpl implements UsersImageService{

    private final UsersImageRepository usersImageRepository;

    @Autowired
    public UsersImageServiceImpl(UsersImageRepository usersImageRepository) {
        this.usersImageRepository = usersImageRepository;
    }

    @Override
    public void saveUserImage(Integer userId, MultipartFile newUserFile) throws IOException {

        String pathToUsersImageDir = Paths.get("").toAbsolutePath().toString() + "/ImageServer/src/main/resources/static/users/";

        String fileName = "user_" + userId + "_" + newUserFile.getOriginalFilename();
        String filePath = pathToUsersImageDir + fileName;

        // Сохраняем файл на диск
        File dest = new File(filePath);
        newUserFile.transferTo(dest);

        Optional<UserImage> userImage = usersImageRepository.findByUserId(userId);
        if(userImage.isPresent()) {

            File file = new File(userImage.get().getUserImagePath());
            boolean deleted = file.delete();

            if (deleted) {
                userImage.get().setUserImagePath(filePath);
                usersImageRepository.save(userImage.get());
            } else {
                System.out.println("Не удалось удалить файл.");
            }
        }
        else {
            // Сохраняем путь к файлу в базу данных
            UserImage newUserImage = new UserImage();
            newUserImage.setUserId(userId);
            newUserImage.setUserImagePath(filePath);
            usersImageRepository.save(newUserImage);
        }
    }

    @Override
    public UserImage getUserImage(Integer userId) {
        return usersImageRepository.findByUserId(userId).orElseThrow(UserImageNotFoundException::new);
    }
}
