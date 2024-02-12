package ru.maksimov.imageserver.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.maksimov.imageserver.models.GroupImage;
import ru.maksimov.imageserver.models.UserImage;

import java.util.Optional;

@Repository
public interface GroupsImageRepository extends JpaRepository<GroupImage, Integer> {
    Optional<GroupImage> findByGroupId(Integer groupId);
}
