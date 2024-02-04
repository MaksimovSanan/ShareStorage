package ru.maksimov.imageserver.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.maksimov.imageserver.models.UserImage;

import java.util.List;

@Repository
public interface UsersImageRepository extends JpaRepository<UserImage, Integer> {

    UserImage findByUserId(Integer userId);
}
