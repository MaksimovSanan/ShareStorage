package ru.maksimov.imageserver.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.maksimov.imageserver.models.GroupImage;
import ru.maksimov.imageserver.models.UserImage;
import ru.maksimov.imageserver.repositories.GroupsImageRepository;
import ru.maksimov.imageserver.util.exceptions.GroupImageNotFoundException;
import ru.maksimov.imageserver.util.exceptions.UserImageNotFoundException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Optional;


// TODO
// absolutely like UsersService, mb need to implementation general interface
@Service
public class GroupsImageServiceImpl implements GroupsImageService{

    private final GroupsImageRepository groupsImageRepository;

    @Autowired
    public GroupsImageServiceImpl(GroupsImageRepository groupsImageRepository) {
        this.groupsImageRepository = groupsImageRepository;
    }

    @Override
    public void saveGroupImage(Integer groupId, MultipartFile newGroupFile) throws IOException {
//        String pathToGroupsImageDir = Paths.get("").toAbsolutePath().toString() + "/ImageServer/src/main/resources/static/groups/";
        String pathToGroupsImageDir = "/resources/static/groups/";

        String fileName = "group_" + groupId + "_" + newGroupFile.getOriginalFilename();
        String filePath = pathToGroupsImageDir + fileName;

        Optional<GroupImage> groupImage = groupsImageRepository.findByGroupId(groupId);
        if(groupImage.isPresent()) {

            File file = new File(pathToGroupsImageDir +groupImage.get().getGroupImagePath());
            boolean deleted = file.delete();

            if (deleted) {
                groupImage.get().setGroupImagePath(fileName);
                groupsImageRepository.save(groupImage.get());
            } else {
                System.out.println("Не удалось удалить файл.");
            }
        }
        else {
            GroupImage newGroupImage = new GroupImage();
            newGroupImage.setGroupId(groupId);
            newGroupImage.setGroupImagePath(fileName);
            groupsImageRepository.save(newGroupImage);
        }

        // Сохраняем файл на диск
        File dest = new File(filePath);
        newGroupFile.transferTo(dest);
    }

    @Override
    public GroupImage getGroupImage(Integer groupId) {
        return groupsImageRepository.findByGroupId(groupId).orElseThrow(GroupImageNotFoundException::new);
    }
}
